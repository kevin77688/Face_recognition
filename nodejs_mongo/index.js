// Inport package
var mongodb = require('mongodb');
var ObjectID = mongodb.ObjectID;
var crypto = require('crypto');
var express = require('express');
var bodyParser = require('body-parser');
const { request, response } = require('express');
const multer = require('multer');

//收到大頭貼時存到uploads資料夾
var imageName;
var storage = multer.diskStorage(
    {
        destination: './uploads/',
        filename: function ( req, file, cb ) {
			imageName = Date.now() + '_' + file.originalname;
			cb(null, imageName);
        }
    }
);
var upload = multer( { storage: storage } );

//password crypto
var getRandomString = function(length){
    return crypto.randomBytes(Math.ceil(length / 2))
        .toString('hex')
        .slice(0,length);
};

var sha512 = function(password, salt){
    var hash = crypto.createHmac('sha512', salt);
    hash.update(password);
    var value = hash.digest('hex');
    return {
        salt:salt,
        passwordHash:value
    };
};

function saltHashPassword(userPassword){
    var salt = getRandomString(16);
    var passwordData = sha512(userPassword, salt);
    return passwordData;
}

function checkHashPassword(userPassword, salt){
    var passwordData = sha512(userPassword, salt);
    return passwordData;
}

// Express services
var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

//MongoDB client
var MongoClient = mongodb.MongoClient;

//Connection URL
var url = 'mongodb://localhost:27017'

MongoClient.connect(url, {useNewParser: true}, function(err, client){
    if (err)
        console.log('Unable to connect to MongoDB', err);
    else{
        // Register
        app.post('/register', (request, response, next)=>{
            var post_data = request.body;

            var plaint_password = post_data.password;
            var hash_data = saltHashPassword(plaint_password);

            var password = hash_data.passwordHash;
            var salt = hash_data.salt;

            var name = post_data.name;
            var email = post_data.email;
            var identification = post_data.identification;
            console.log("has identification");
            var insertJson = {
                'email': email,
                'password': plaint_password, //改成不加密
                'salt': salt,
                'name': name,
                'identification': identification
            };

            var db = client.db('nodejsTest');

            db.collection('user')
                .find({'email': email}).count(function(err, number){
                    if (number != 0){
                        response.json('Email already exists');
                        console.log('Email already exists');
                    }
                    else{
                        // Insert Data
                        db.collection('user')
                            .insertOne(insertJson, function(error, res){
                                response.json('Registration success');
                                console.log('Registration success');
                            })
                    }
                })
        });

        // Login
        app.post('/login', (request, response, next)=>{
            var post_data = request.body;
            var email = post_data.email;
            var userPassword = post_data.password;

            var db = client.db('nodejsTest');

            db.collection('user')
                .find({'email': email}).count(function(err, number){
                    if (number == 0){
                        response.json('Email not exists');
                        console.log('Email not exists');
                    }
                    else{
                        // Insert Data
                        db.collection('user')
                            .findOne({'email': email}, function(err, user){
                                var salt = user.salt;
                                var hashed_password = checkHashPassword(userPassword, salt).passwordHash;
                                var encryped_password = user.password;
                                var identification = user.identification;
                                // if(hashed_password == encryped_password){
                                    if(identification == "student"){
                                        response.json('Login student');
                                    }else{
                                        response.json('Login teacher');
                                    }
                                    console.log('Login success');
                                // }
                                // else{
                                //     response.json('Login failed');
                                //     console.log('Login failed');
                                // }
                            })
                    }
                })
        });

        app.post('/findUserName', (request, response, next)=>{
            var post_data = request.body;
            var email = post_data.email;
            var db = client.db('nodejsTest');
            console.log(post_data.email);
            db.collection('user')
                            .findOne({'email': email}, function(err, user){
                                console.log("findUserName：",user.name);
                                var userData = {
                                    'name': user.name,
                                    'id': user._id
                                };
                                response.json(userData);
                            })
        });

        app.post('/findStudent', (request, response, next)=>{
            var post_data = request.body;
            var class_name = post_data.class_name;
            var db = client.db('nodejsTest');
            console.log(class_name);
            var data,student_list;
            function findClassNumber(){
                const  thing = db.collection("course").findOne({ name: class_name });
                // console.log(thing);
                return thing;
            }
            function findStudent(class_code) {
                var thing = db.collection("studentCourse").find({'courseId': class_code}).toArray();
                return thing;
            }
            async function find() {
                data = await findClassNumber();
                student_list = await findStudent(data._id);
                console.log("data:",data._id);
                console.log("student_list:",student_list);
            }
            find();
            
        });
        
        app.post('/findUserClass', (request, response, next)=>{
            var post_data = request.body;
            var id =parseInt(post_data.id)  ;
            var db = client.db('nodejsTest');
            console.log("post findUserClass: 888888");
            console.log(post_data);
            var userData = {};
            userData.class = [];
            db.collection("course").find({'teacherId': id}).toArray(function(err, result){
                if (err) throw err;
                for(let i=0;i<result.length;i++)
                    userData.class.push(result[i].name)
                response.json(userData);
            });
        });

        app.post('/findUserClassDate', (request, response, next)=>{
            var post_data = request.body;
            var class_name =post_data.class_name;
            var db = client.db('nodejsTest');
            console.log("post findUserClass: 99999");
            console.log("findUserClassDate：",class_name);
            var classData = {};
            classData.date = [];
            classData.name = class_name;
            db.collection("courseDate").find({'classId': class_name}).toArray(function(err, result){
                for(let j=0;j<result.length;j++)
                    classData.date.push(result[j].date);
                console.log("4",classData);
                response.json(classData);
            });
        });
		
		// student upload images
		app.post('/studentUpload', upload.single('image'), async(request, response, next)=>{
			console.log(request.body);
			console.log(request.file);
			console.log(request.file.originalname);
			var db = client.db('nodejsTest');
			db.collection('avatar').insertOne({'userId': request.body._id, 'imageName': imageName}, function(error, res){
                                response.json('Upload success');
                                console.log('Upload success');
                            })
		});

        //Web server
        app.listen(3000, ()=>{
            console.log('Connected to MongoDB server with port 3000')
        })
    }
});