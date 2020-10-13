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
	// 100 Database insertion error
	// 200 student login
    // 201 teacher login
    // 202 register success
	// 203 Get data success
    // 400 login password error
    // 401 register email exist
	// 402 login email not exist
	// 403 Get data failed
	// 405 register id exist
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
			var numberOfSameEmail = await findUserExistenceUsingEmail();
			if (numberOfSameEmail != 0){
				userResponse.description = "Register email exist";
				userResponse.status = 401;
				console.log('Email already exists');
			}
			var numberOfSameId = await findUserExistUsingId();
			if (numberOfSameId != 0){
				userResponse.description = "Register id exist";
				userResponse.status = 405;
				console.log('Id already exists');
			}
			if (numberOfSameEmail == 0 && numberOfSameId == 0){
				// Insert Data
				db.collection('user').insertOne(insertJson, function(error, res){
					if (!error){
						userResponse.username = name;
						userResponse.userId = _id;
						userResponse.description = "Register success";
						userResponse.status = 202;
						console.log('Registration success');
					}
					else {
						userResponse.description = "Database insertion error";
						userResponse.status = 100;
					}
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
			var numberOfExistence = await findUserExistenceUsingEmail(email);
			if (numberOfExistence == 0){
				userResponse.description = 'Email not exists';
				userResponse.status = "402";
				console.log('Login email not exists');
			}else{
				var user = await findUserDataUsingEmail(email);
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
							userResponse.courses = {};
							var courses = await findCoursesUsingTeacherId(user._id);
							for (var i = 0; i < courses.length; i++){
								userResponse.courses[courses[i]._id] = courses[i].name;
							}
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
		
		function findUserExistenceUsingEmail(email){
			var db = client.db('nodejsTest');
            var number = db.collection('user').find({'email': email}).count();
			return number;
		}
		
		function findUserDataUsingEmail(email){
			var db = client.db('nodejsTest');
			var user = db.collection('user').findOne({'email': email});
			return user;
		}
		
		function findUserExistenceUsingId(id){
			var db = client.db('nodejsTest');
			var user = db.collection('user').findone({'_id': id});
			return user;
		}
		
		function findStudentsUsingCourseId(course_id) {
			var db = client.db('nodejsTest');
			var students = db.collection("studentCourse").find({_id: course_id}).toArray();
			return students;
		}
		
		function findCoursesUsingTeacherId(teacher_id){
			var db = client.db('nodejsTest');
			var courses = db.collection('course').find({teacherId: teacher_id}).toArray();
			return courses;
		}

        app.post('/uploadAttendanceList', async(request, response, next)=>{
            var post_data = request.body;
			var course_id = post_data.courseId;
			var date = post_data.date;
			var isRecorded = await CheckCourseDateRecorded(course_id, date);
			if (isRecorded){
				
			}
			else{
				
			}
        });
		
		app.post('/rollCallUpdate', async(request, response, next)=>{
            var post_data = request.body;
			var course_id = post_data.courseId;
			var date = post_data.date;
			var isRecorded = await CheckCourseDateRecorded(course_id, date);
			console.log("called!");
			if (isRecorded){
				
			}
			else{
				
			}
        });

        app.post('/teacherGetCourseAttendance', async(request, response, next)=>{
			var userResponse = {};
            var post_data = request.body;
			var date = post_data.date;
			var course_id = post_data.courseId;
            var isRecorded = await CheckCourseDateRecorded(course_id, date);
			userResponse.attendance = {}
			if (!isRecorded){
				var studentList = await FindStudentListUsingCourseId(course_id);
				for (let i = 0; i < studentList.length; i++){
					userResponse.attendance[studentList[i].studentId] = {name: studentList[i].studentDetails[0].name, attendance: '-1'};
				}
				userResponse.isRecorded = false;
			}
			else {
				var attendance = await FindAttendanceUsingDateAndCourseId(course_id, date);
				for (let i = 0; i < attendance.length; i++){
					userResponse.attendance[attendance[i].studentId] = {name: attendance[i].studentDetails[0].name, attendance: attendance[i].attendance}
				}
				userResponse.isRecorded = true;
			}
			console.log(userResponse);
			response.json(userResponse);
        });
		
		function CheckCourseDateRecorded(course_id, date){
			var db = client.db('nodejsTest');
			var courseDate = db.collection('courseDate').findOne({date: date, courseId: course_id});
			return courseDate.isRecord;
		}
		
		function FindStudentListUsingCourseId(course_id){
			var db = client.db('nodejsTest');
			const pipeline = [
				{
					'$lookup':
					{
						from: 'user',
						localField: 'studentId',
						foreignField: '_id',
						as: 'studentDetails'
					}
				},
				{
					'$match':
					{
						courseId: course_id
					}
				}
			]
			var studentList = db.collection('studentCourse').aggregate(pipeline).toArray();
			return studentList;
		}
		
		function FindAttendanceUsingDateAndCourseId(course_id, date){
			var db = client.db('nodejsTest');
			const pipeline = [
				{
					'$lookup':
					{
						from: 'user',
						localField: 'studentId',
						foreignField: '_id',
						as: 'studentDetails'
					}
				},
				{
					'$match':
					{
						courseId: course_id
					}
				}
			]
			var attendance = db.collection('attendance').aggregate(pipeline).toArray();
			return attendance;
		}

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

        app.post('/studentCheckAttendance', async(request, response, next)=>{
			var userResponse = {};
            var post_data = request.body;
            var student_id = post_data.studentId ;
            var attendance = await findAttendanceUsingStudentId(student_id);
			if (!attendance){
				userResponse.description = "Get attendance failed";
				userResponse.status = 403;
			}
			else {
				userResponse.description = "Get attendance success"
				userResponse.status = 203;
				userResponse.courses = {};
				for(var i = 0; i < attendance.length; i++){
					console.log(attendance[i]);
					userResponse.courses[i] = {id: attendance[i].courseId, name: attendance[i].courseDetails[0].name, date: attendance[i].date, attendance: attendance[i].attendance};
				}
			}
			console.log(userResponse)
			console.log(userResponse.courses)
            response.json(userResponse);
        });
		
		function findAttendanceUsingStudentId(id){
			var db = client.db('nodejsTest');
			const pipeline = [
				{
					'$lookup':
					{
						from: 'course',
						localField: 'courseId',
						foreignField: '_id',
						as: 'courseDetails'
					}
				},
				{
					'$match':
					{
						studentId: id
					}
				}
			]
			attendance = db.collection('attendance').aggregate(pipeline).toArray();
			return attendance;
		}

        app.post('/teacherFindCourseDate', async(request, response, next)=>{
			var userResponse = {};
            var post_data = request.body;
            var course_id = post_data.courseId;
            console.log("Teacher find course date：",course_id);
            var courseDates = await findCourseDatesUsingCourseId(course_id);
            userResponse.status = "203";
			userResponse.description = "Find data success";
			userResponse.dates = {};
			console.log(courseDates);
			for (var i = 0; i < courseDates.length; i++){
				userResponse.dates[i] = courseDates[i].date;
			}
			console.log(userResponse);
			response.json(userResponse);
        });
		
		function findCourseDatesUsingCourseId(course_id){
			var db = client.db('nodejsTest');
			var courseDates = db.collection("courseDate").find({'courseId': course_id}).toArray();
			return courseDates;
		}
		
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