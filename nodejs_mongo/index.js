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
	
	// Response code : 
    // Login password wrong:402
	// 200 student login
    // 201 teacher login
    // 202 register success
    // 400 login password error
    // 401 register email exist
	// 402 login email not exist
    if (err)
        console.log('Unable to connect to MongoDB', err);
    else{
        // Register
        app.post('/register', async(request, response, next)=>{
			var userResponse = {};
            var post_data = request.body;

            var plaint_password = post_data.password;
            var hash_data = saltHashPassword(plaint_password);

            var password = hash_data.passwordHash;
            var salt = hash_data.salt;

            var name = post_data.name;
            var email = post_data.email;
			var _id = post_data._id;
            var identification = "student";
			if (_id[0] == 'a'){
				identification = "teacher"
			}
            console.log("has identification");
            var insertJson = {
                'email': email,
                'password': plaint_password, //改成不加密
                'salt': salt,
                'name': name,
				'_id': _id,
                'identification': identification
            };

            var db = client.db('nodejsTest');
			var numberOfExistence = await findUserExistenceFromEmail();
			if (numberOfExistence != 0){
				userResponse.description = "Register email exist"
				userResponse.status = 401;
				console.log('Email already exists');
			}
			else{
				// Insert Data
				db.collection('user')
					.insertOne(insertJson, function(error, res){
						userResponse.username = name;
						userResponse.userId = _id;
						userResponse.description = "Register success";
						userResponse.status = 202;
						console.log('Registration success');
					})
			}
			response.json(userResponse);
        });

        // Login
        app.post('/login', async(request, response, next)=>{
			userResponse = {};
            var post_data = request.body;
            var email = post_data.email;
            var userPassword = post_data.password;
			var numberOfExistence = await findUserExistenceFromEmail(email);
			if (numberOfExistence == 0){
				userResponse.description = 'Email not exists';
				userResponse.status = "402";
				console.log('Login email not exists');
			}else{
				var user = await findUserDataFromEmail(email);
				if (user){
					if (userPassword != user.password){
						console.log("login password error");
						userResponse.description = 'Login password error !';
						userResponse.status = "400";
					}
					else{
						console.log("Login success");
						userResponse.description = 'Login success';
						if (user.identification == "student"){
							console.log("Student login");
							userResponse.description = "Student login";
							userResponse.status = 200;
							
						}
						else if (user.identification == "teacher"){
							userResponse.description = "Teacher login";
							userResponse.status = 201;
						}
						userResponse.username = user.name;
						userResponse.userId = user._id;
						console.log("username : " + user.name + " login");
					}
				}
				else {
					console.log("user not found!");
				}
			}
			console.log(userResponse);
			response.json(userResponse);
		});
		
		function findUserExistenceFromEmail(email){
			var db = client.db('nodejsTest');
            var number = db.collection('user').find({'email': email}).count();
			return number;
		}
		
		function findUserDataFromEmail(email){
			var db = client.db('nodejsTest');
			var userResponse = {};
			var user = db.collection('user').findOne({'email': email})
			return user;
		}

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
            console.log("findStudent",class_name);
            var data,student_list;
            var student = {};
            student.name = [];
            function findClassNumber(){
                const  thing = db.collection("course").findOne({ name: class_name });
                // console.log(thing);
                return thing;
            }
            function findStudentCode(class_code) {
                var thing = db.collection("studentCourse").find({courseId: class_code}).toArray();
                return thing;
            }
            function findStudent(student) {
                console.log("student99:",student);
                const  thing = db.collection("user").findOne({ _id: student });
                console.log("thing:",thing);

                return thing;
            }
            async function find() {
                data = await findClassNumber();
                student_list = await findStudentCode(data._id);
                for(let i=0;i<student_list.length;i++){
                    var data = await findStudent(student_list[i].studentId);
                    student.name.push(data.name);
                }
                console.log("data:",data._id);
                console.log("student_list:",student_list);
                console.log("student:",student);
                response.json(student);
            }
            find();
        });

        app.post('/rollCallUpdate', (request, response, next)=>{
            var post_data = request.body;
            var class_data = post_data.class_data;
            var class_name = post_data.class_name;
            var student_data = post_data.student_data;
            var roll_call_data = post_data.roll_call_data;

            console.log(class_data);
            console.log(class_name);
            console.log(student_data);
            console.log(roll_call_data);
            var db = client.db('nodejsTest');
            function isRollCallExist(data){
                var thing = db.collection("rollcall").deleteOne(data);
                return thing;
            }
            async function operation() {
                for(let i=0;i<student_data.length;i++){
                    let name = student_data[i].replace("\"","");
                    name = name.replace("\"","");
                    let insert_data = {date: class_data, class: class_name, name: name,  status: roll_call_data[i]};
                    await isRollCallExist({date: class_data, class: class_name, name: name});
                    db.collection("rollcall").insertOne(insert_data);
                }
                response.json("success");
            }
            operation();
        });

        app.post('/getRollCall', (request, response, next)=>{
            var post_data = request.body;
            var class_data = post_data.class_data;
            var class_name = post_data.class_name;

            console.log(class_data);
            console.log(class_name);
            var userData = {};
            userData.student_name = [];
            userData.student_status = [];
            var db = client.db('nodejsTest');
            db.collection("rollcall").find({date: class_data, class: class_name}).toArray(function(err, result){
                if (err) throw err;
                for(let i=0;i<result.length;i++){
                    userData.student_name.push(result[i].name)
                    userData.student_status.push(result[i].status)
                }
                console.log(userData);
                response.json(userData);
            });
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

        app.post('/findStudentClass', (request, response, next)=>{
            var post_data = request.body;
            var id =parseInt(post_data.id)  ;
            var db = client.db('nodejsTest');
            var userData = {};
            userData.class = [];
        
            var student_calss_code = [];
            function findClassCode(id){
                const thing = db.collection("studentCourse").find({studentId: id}).toArray();
                return thing;
            }
            function findClassName(code){
                const thing = db.collection("course").findOne({_id: code});
                return thing;
            }
            async function operation() {
                student_class_code = await findClassCode(id);
                for(let i=0;i<student_class_code.length;i++){
                    let a = await findClassName(student_class_code[i].courseId)
                    userData.class.push(a.name);
                }

                response.json(userData);
            }
            operation();
        });

        app.post('/findRollCall', (request, response, next)=>{
            var post_data = request.body;
            var name = post_data.name ;
            var db = client.db('nodejsTest');
            var userData = {};
            userData.class = [];
            userData.date = [];
            userData.status = [];
            console.log("good",name);
            db.collection("rollcall").find({'name': name}).toArray(function(err, result){
                for(let i=0;i<result.length;i++){
                    userData.class.push(result[i].class);
                    userData.date.push(result[i].date);
                    userData.status.push(result[i].status);
                }
                console.log("good",userData);


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