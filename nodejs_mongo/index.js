// Inport package
var mongodb = require('mongodb');
var ObjectID = mongodb.ObjectID;
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
    // Register success :   200
    // Login student :      201
    // Login teacher :      202
    // Email exists :       400
    // Login error :        401
    // Login password wrong:402

    if (err)
        console.log('Unable to connect to MongoDB', err);
    else{
        var userResponse = {};
        // Register
        app.post('/register', (request, response, next)=>{
            var post_data = request.body;
            var username = post_data.username;
            var password = post_data.password;
            var email = post_data.email;
            var identification = "student";
            if (username.substring(0,1) == "a")
                identification = "teacher"

            console.log("has identification");
            var insertJson = {
                'username': username,
                'password': password,
                'email': email,
                'identification': identification
            };

            var db = client.db('nodejsTest');

            db.collection('user')
                .find({'email': email}).count(function(err, number){
                    if (number != 0){
                        userResponse.description = "Email already exists";
                        userResponse.status = "400";
                        console.log('Email already exists');
                    }
                    else{
                        // Insert Data
                        db.collection('user')
                            .insertOne(insertJson, function(error, res){
                                userResponse.description = 'Registration success';
                                userResponse.status = "200";
                                console.log('Registration success');
                            })
                    }
                })
            response.json(userResponse);
        });

        // Login
        app.post('/login', (request, response, next)=>{
            var post_data = request.body;
            var email = post_data.email;
            var password = post_data.password;
            var db = client.db('nodejsTest');

            db.collection('user')
                .find({'email': email}).count(function(err, number){
                    if (number == 0){
                        userResponse.description = 'Email not exists';
                        userResponse.status = "401";
                        console.log('Email not exists');
                    }else{
                        db.collection('user').findOne({'email': email}, function(err, user){
                            if (password != user.password){
                                userResponse.description = 'Login error !';
                                userResponse.status = "402";
                            }
                            else{
                                userResponse.description = 'Login success';
                                if (user.identification == "student")
                                    userResponse.status = "201";
                                else if (user.identification == "teacher")
                                    userResponse.status = "202";
                                userResponse.username = user.username;
                                userResponse.identification = user.identification;
                                console.log("username : " + user.username + " login");
                            }})
                    }
                    response.json(userResponse);
        })});

        app.post('/findUserClass', (request, response, next)=>{
            // var post_data = request.body;
            // var id =parseInt(post_data.id)  ;
            // var db = client.db('nodejsTest');
            // console.log("post findUserClass: 888888");
            // console.log(post_data);

            // db.collection('course')
            //                 .findOne({'teacherId': id}, function(err, user){
            //                     console.log("post findUserClass: ",user);
            //                     // var userData = {
            //                     //     'name': user.name,
            //                     //     'id': user._id
            //                     // };
            //                     // response.json(userData);
            //                     response.json(user.name);
            //                 })
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
