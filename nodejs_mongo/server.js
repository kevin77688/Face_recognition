// Inport package
var mongodb = require('mongodb');
var ObjectID = mongodb.ObjectID;
var crypto = require('crypto');
var express = require('express');
var bodyParser = require('body-parser');
const { request, response } = require('express');
const multer = require('multer');
const fs = require('fs');

//收到大頭貼時存到uploads資料夾
var storage = multer.diskStorage(
    {
        destination: './uploads/',
        filename: function ( req, file, cb ) {
			var imageName = file.originalname + ".png";
			cb(null, imageName);
        }
    }
);

var storage2 = multer.diskStorage(
    {
        destination: './teacherUploads/',
        filename: function ( req, file, cb ) {
			var imageName = file.originalname + ".png";
			cb(null, imageName);
        }
    }
);

var upload = multer( { storage: storage } );
var upload2 = multer( { storage: storage2 } );

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

// remote
var url = 'mongodb+srv://admin:BYvnxe7GKR7yTHF4@cluster0.bso7x.gcp.mongodb.net/nodejsTest?retryWrites=true&w=majority'

// localhost
// var url = 'mongodb://localhost:27017'

var dbName = 'nodejsTest'

MongoClient.connect(url, {useNewParser: true}, function(err, client){
	
	// Response code : 
	// 200 student login
    // 201 teacher login
    // 202 register success
	// 203 Get data success
	// 204 insert data success
	// 205 update data success
	// 206 course found, had joined already
	// 207 course found
    // 400 login password error
    // 401 register email exist
	// 402 login email not exist
	// 403 Get data failed
	// 405 register id exist
	// 406 insert data failed
	// 407 course not found
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

            var db = client.db(dbName);
			var numberOfSameEmail = await findUserExistenceUsingEmail();
			if (numberOfSameEmail != 0){
				userResponse.description = "Register email exist";
				userResponse.status = 401;
				console.log('Email already exists');
			}
			var numberOfSameId = await findUserExistenceUsingId();
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
						userResponse.description = "insert data error";
						userResponse.status = 406;
						console.log("insert data error");
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
			var db = client.db(dbName);
            var number = db.collection('user').find({'email': email}).count();
			return number;
		}
		
		function findUserDataUsingEmail(email){
			var db = client.db(dbName);
			var user = db.collection('user').findOne({'email': email});
			return user;
		}
		
		function findUserExistenceUsingId(id){
			var db = client.db(dbName);
			var number = db.collection('user').find({'_id': id}).count();
			return number;
		}
		
		function findCoursesUsingTeacherId(teacher_id){
			var db = client.db(dbName);
			var courses = db.collection('course').find({teacherId: teacher_id}).toArray();
			return courses;
		}

        app.post('/uploadAttendanceList', async(request, response, next)=>{
			var userResponse = {};
            var post_data = request.body;
			var jsonData = JSON.parse(post_data.studentAttendantList);
			console.log(jsonData);
			var course_id = jsonData.courseId;
			var date = jsonData.courseDate;
			var studentList = jsonData.students;
			var courseDate = await GetCourseDateByIdAndDate(course_id, date);
			var isRecorded = courseDate.isRecord;
			var db = client.db(dbName);
			if (isRecorded){
				await db.collection('attendance').remove({courseId: course_id, date: date});
			}
			userResponse.status = 204;
			var insertDocs = [];
			for (const [key, value] of Object.entries(studentList)) {
				insertDocs.push({studentId: key, date: date, courseId: course_id, attendance: value.toString()});
			}
			userResponse.result = await db.collection('attendance').insertMany(insertDocs);
			var filter = {courseId: course_id, date: date};
			var updateValue = { $set: { isRecord: true } };
			await db.collection('courseDate').updateOne(filter, updateValue);
			console.log(userResponse);
			response.json(userResponse);
        });

        app.post('/teacherGetCourseAttendance', async(request, response, next)=>{
			var userResponse = {};
            var post_data = request.body;
			var date = post_data.courseDate;
			var course_id = post_data.courseId;
            var courseDate = await GetCourseDateByIdAndDate(course_id, date);
			var isRecorded = courseDate.isRecord;
			userResponse.attendance = {}
			//預設都沒有點名，從學生課程清單裡面抓學生清單，把點名欄位設為-1
			var studentList = await FindStudentListUsingCourseId(course_id);
			for (let i = 0; i < studentList.length; i++){
				userResponse.attendance[studentList[i].studentId] = {name: studentList[i].studentDetails[0].name, attendance: '-1'};
			}
			userResponse.isRecorded = false;
			//如果有點名，把有點名的人設為已存在值
			if (isRecorded) {
				var attendance = await FindAttendanceUsingDateAndCourseId(course_id, date);
				console.log(date)
				console.log(attendance)
				for (let i = 0; i < attendance.length; i++){
					userResponse.attendance[attendance[i].studentId] = {name: attendance[i].studentDetails[0].name, attendance: attendance[i].attendance}
				}
				userResponse.isRecorded = true;
			}
			console.log(userResponse);
			response.json(userResponse);
        });
		
		function GetCourseDateByIdAndDate(course_id, date){
			var db = client.db(dbName);
			var courseDate = db.collection('courseDate').findOne({date: date, courseId: course_id});
			return courseDate;
		}
		
		function FindStudentListUsingCourseId(course_id){
			var db = client.db(dbName);
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
			var db = client.db(dbName);
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
						courseId: course_id,
						date: date
					}
				}
			]
			var attendance = db.collection('attendance').aggregate(pipeline).toArray();
			return attendance;
		}

        app.post('/findStudentClass', (request, response, next)=>{
            var post_data = request.body;
            var id =parseInt(post_data.id)  ;
            var db = client.db(dbName);
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
			var db = client.db(dbName);
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
			var db = client.db(dbName);
			var courseDates = db.collection("courseDate").find({'courseId': course_id}).toArray();
			return courseDates;
		}
		
		// student upload images
		app.post('/studentUpload', upload.single('image'), async(request, response, next)=>{
			console.log(request.body);
			console.log(request.file);
			var db = client.db(dbName);
			var userResponse = {};
			var imageName = request.file.originalname + ".png";
			console.log("before require")
			let spawn = require("child_process").spawn
			console.log("before detection")
			let process = spawn('python', [
				"./detect.py",
				request.body.userId,
			])
			console.log("after detection")
			process.stdout.on('data', async(data)=>{
				const onlyOneFace = JSON.parse(data)
				console.log(onlyOneFace)
				if (onlyOneFace){
					await db.collection('avatar').remove({userId: request.body.userId});
					await db.collection('avatar').insertOne({'userId': request.body.userId, 'imageName': imageName})
					userResponse.status = 208;
				}
				else {
					userResponse.status = 408;
				}
				try{	
					fs.unlinkSync(__dirname + "/uploads/" + imageName);
				} catch (err){
					
				}
				console.log(userResponse);
				response.json(userResponse);
			});
		});
		
		// student check avatar
		app.post('/studentCheckAvatar', async(request, response, next)=>{
			var userResponse = {};
			var post_data = request.body;
			var student_id = post_data.studentId;
			var index = post_data.index;
			var db = client.db(dbName);
			var avatars = await db.collection('avatar').find({userId: student_id}).toArray();
			if (avatars.length == 0){
				response.json("avatar not exist");
			}
			else {
				response.sendFile(__dirname + "/data/" + avatars[index].imageName);
			}
		});
		
		// student get total avatar
		app.post('/studentGetTotalAvatar', async(request, response, next)=>{
			var userResponse = {};
			var post_data = request.body;
			var student_id = post_data.studentId;
			console.log(student_id);
			var db = client.db(dbName);
			userResponse.amount = await db.collection('avatar').find({userId: student_id}).count();
			console.log(userResponse);
			response.json(userResponse);
		});
		
		// student delete avatar
		app.post('/studentDeleteAvatar', async(request, response, next)=>{
			var userResponse = {};
			var post_data = request.body;
			var student_id = post_data.studentId;
			var index = post_data.index;
			console.log(student_id);
			var db = client.db(dbName);
			var avatars = await db.collection('avatar').find({userId: student_id}).toArray();
			await db.collection('avatar').deleteOne({userId: student_id, imageName: avatars[index].imageName});
			try{	
				fs.unlinkSync(__dirname + "/data/" + avatars[index].imageName);	
			} catch (err){
				
			}
			response.json(userResponse);
		});
		
		// try python
		app.post('/pythonAdd', (request, response, next)=>{
			let spawn = require("child_process").spawn

			let process = spawn('python', [
				"./process.py",
				request.body.number1,
				request.body.number2
			])

			process.stdout.on('data', (data) => {
				const parsedString = JSON.parse(data)
				console.log(parsedString)
				response.json(parsedString)
			})
		});
		
		// get course id and using it to get student avatars, then pass them to python file
		app.post('/giveStudentAvatarsToPythonProcess', async(request, response, next)=>{
			var course_id = request.body.courseId;
			console.log(course_id);
			var db = client.db(dbName);
			var studentList = await FindStudentListWithAvatarUsingCourseId(course_id)//db.collection('studentCourse').find({courseId: course_id}).toArray();
			console.log(studentList)
			let spawn = require("child_process").spawn
			let testArray = [];
			for (let i = 0; i < studentList.length; i++){
				for (let j = 0; j < studentList[i].avatars.length; j++){
					testArray.push(studentList[i].studentId);
					testArray.push(studentList[i].avatars[j].imageName)
				}
			}
			let testJson = {"name": "kenny"}
			let process = spawn('python', [
				"./process2.py",
				testArray
			])

			process.stdout.on('data', (data) => {
				const parsedString = JSON.parse(data)
				console.log(parsedString)
				response.json(parsedString)
			})
		});
		
		function FindStudentListWithAvatarUsingCourseId(course_id){
			var db = client.db(dbName);
			const pipeline = [
				{
					'$lookup':
					{
						from: 'avatar',
						localField: 'studentId',
						foreignField: 'userId',
						as: 'avatars'
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
		
		//teacherUpload
		app.post('/teacherUpload', upload2.single('image'), async(request, response, next)=>{
			console.log(request.body);
			console.log(request.file);
			var db = client.db(dbName);
			var course_id = request.file.originalname;
			var date = request.body.date;
			console.log(course_id);
			var db = client.db(dbName);
			var studentList = await FindStudentListWithAvatarUsingCourseId(course_id)
			console.log(studentList);
			let spawn = require("child_process").spawn
			let filePathList = [];
			let noAvatar = true;
			for (let i = 0; i < studentList.length; i++){
				for (let j = 0; j < studentList[i].avatars.length; j++){
					filePathList.push("./data/" + studentList[i].studentId + ".png");
					noAvatar = false;
				}
			}
			if (noAvatar){
				console.log("[]");
				response.json("[]");
				return;
			}
			let process = spawn('python', [
				"./recognize.py",
				course_id,
				filePathList
			])
			process.stdout.on('data', async(data) => {
				const parsedstudentIdList = JSON.parse(data)
				const parsedString = data
				insertDocs = [];
				for (var i = 0; i < parsedstudentIdList.length; i++) {
					insertDocs.push({studentId: parsedstudentIdList[i], date: date, courseId: course_id, attendance: "0"});
				}
				if (insertDocs.length != 0){
					await db.collection('attendance').remove({studentId: {'$in':parsedstudentIdList}, courseId: course_id, date: date});
					await db.collection('attendance').insertMany(insertDocs);
					var filter = {courseId: course_id, date: date};
					var updateValue = { $set: { isRecord: true } };
					await db.collection('courseDate').updateOne(filter, updateValue);	
				}
				var imageName = request.file.originalname + ".png";
				try{	
					fs.unlinkSync(__dirname + "/uploads/" + imageName);
				} catch (err){
					
				}
				console.log(parsedstudentIdList)
				response.json(parsedstudentIdList)
			})
		});
		
		// student search course
		app.post('/studentSearchCourse', async(request, response, next)=>{
			var userResponse = {};
			var post_data = request.body;
			var course_id = post_data.courseId;
			var student_id = post_data.studentId;
			var db = client.db(dbName);
			var course = await db.collection('course').findOne({_id: course_id});
			if (course == null){
				userResponse.status = 407;
			}
			else {
				var hadJoined = await db.collection('studentCourse').find({studentId: student_id, courseId: course_id}).count();
				if (hadJoined){
					userResponse.status = 206;
					userResponse.course = course;
				}
				else {
					userResponse.status = 207;
					userResponse.course = course;
				}
			}
			console.log(userResponse);
			response.json(userResponse);
		});
		
		//student join course
		app.post('/studentAddCourse', async(request, response, next)=>{
			var userResponse = {};
			var post_data = request.body;
			var student_id = post_data.studentId;
			var course_id = post_data.courseId;
			var db = client.db(dbName);
			await db.collection('studentCourse').insertOne({studentId: student_id, courseId: course_id, score: -1, attendance: 0});
			userResponse.status = 204;
			response.json(userResponse);
		});

        //Web server
        app.listen(3000, ()=>{
            console.log('Connected to MongoDB server with port 3000')
        })
    }
});
