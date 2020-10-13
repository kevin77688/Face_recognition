use nodejsTest

//建立課程資料

db.Students.drop();
db.professor.drop();
db.rollcall.drop();
db.testUri.drop();
db.student.drop();


db.course.drop();
db.course.insertMany([

    {_id: '268986', teacherId: '00001', requiredOrElective: "elective", name: "作業系統", englishName: "Operating Systems", code: '5903206', stage: 1, credits: 3},

    {_id: '268987', teacherId: '00001', requiredOrElective: "required", name: "實務專題(二)", englishName: "Special Project(II)", code: "5903208", stage: 1, credits: 3},

    {_id: '269469', teacherId: '00002', requiredOrElective: "elective", name: "財務管理", englishName: "Finance Management", code: "5702007", stage: 1, credits: 3},

    {_id: '269481', teacherId: '00002', requiredOrElective: "required", name: "物件導向程式設計實習", englishName: "Object-Oriented Programming Labs", code: "5902209", stage: 1, credits: 2},

    {_id: '271547', teacherId: '00003', requiredOrElective: "required", name: "體育", englishName: "Physical Education", code: "1001001", stage: 6, credits: 0},

    {_id: '272986', teacherId: '00003', requiredOrElective: "elective", name: "設計樣式", englishName: "Design Pattern", code: "5904322", stage: 1, credits: 3},

    {_id: '273941', teacherId: '00004', requiredOrElective: "required", name: "智慧財產權", englishName: "Intellectual Property", code: "1410145", stage: 1, credits: 2},

    {_id: '274128', teacherId: '00004', requiredOrElective: "required", name: "人工智慧概論", englishName: "Introduction to Artificial Intelligence", code: "1418015", stage: 1, credits: 2},

    {_id: '274202', teacherId: '00005', requiredOrElective: "elective", name: "雲端應用實務", lishName: "Cloud Applications Practice", code: "5902310", stage: 1, credits: 3},

    {_id: '274250', teacherId: '00005', requiredOrElective: "elective", name: "職涯進擊講座", englishName: "Lecture: Increase capacity for career exploration", code: "AA04017", stage: 1, credits: 1},

    {_id: '274340', teacherId: '00005', requiredOrElective: "elective", name: "國際觀培養講座", englishName: "Feast for the global perspective", code: "AA01023", stage: 1, credits: 1},

]);


//建立課程資料

db.courseDate.drop();

db.courseDate.insertMany([

    {recordId:1, courseId: "268986", date: "2020-09-07",isRecord: false},
	{recordId:2, courseId: "268986", date: "2020-09-13",isRecord: false},
	{recordId:3, courseId: "268986", date: "2020-09-20",isRecord: false},
	{recordId:4, courseId: "268986", date: "2020-09-27",isRecord: false},

    {recordId:5, courseId: "268987", date: "2020-09-01",isRecord: false},
	{recordId:6, courseId: "268987", date: "2020-09-08",isRecord: false},
	{recordId:7, courseId: "268987", date: "2020-09-15",isRecord: false},
	{recordId:8, courseId: "268987", date: "2020-09-22",isRecord: false},

　　{recordId:9, courseId: "269469", date: "2020-09-02",isRecord: false},
	{recordId:10, courseId: "269469", date: "2020-09-09",isRecord: false},
	{recordId:11, courseId: "269469", date: "2020-09-16",isRecord: false},
	{recordId:12, courseId: "269469", date: "2020-09-23",isRecord: false},

　　{recordId:13, courseId: "269481", date: "2020-09-03",isRecord: false},
	{recordId:14, courseId: "269481", date: "2020-09-10",isRecord: false},
	{recordId:15, courseId: "269481", date: "2020-09-17",isRecord: false},
	{recordId:16, courseId: "269481", date: "2020-09-24",isRecord: false},

　　{recordId:17, courseId: "271547", date: "2020-09-04",isRecord: false},
	{recordId:18, courseId: "271547", date: "2020-09-11",isRecord: false},
	{recordId:19, courseId: "271547", date: "2020-09-18",isRecord: false},
	{recordId:20, courseId: "271547", date: "2020-09-25",isRecord: false},

　　{recordId:21, courseId: "272986", date: "2020-10-05",isRecord: false},
	{recordId:22, courseId: "272986", date: "2020-10-12",isRecord: false},
	{recordId:23, courseId: "272986", date: "2020-10-19",isRecord: false},
	{recordId:24, courseId: "272986", date: "2020-10-26",isRecord: false},

　　{recordId:25, courseId: "273941", date: "2020-10-06",isRecord: false},
	{recordId:26, courseId: "273941", date: "2020-10-13",isRecord: false},
	{recordId:27, courseId: "273941", date: "2020-10-20",isRecord: false},
	{recordId:28, courseId: "273941", date: "2020-10-27",isRecord: false},

　　{recordId:29, courseId: "274128", date: "2020-10-07",isRecord: false},
	{recordId:30, courseId: "274128", date: "2020-10-14",isRecord: false},
	{recordId:31, courseId: "274128", date: "2020-10-21",isRecord: false},
	{recordId:32, courseId: "274128", date: "2020-10-28",isRecord: false},

　　{recordId:33, courseId: "274202", date: "2020-10-01",isRecord: false},
	{recordId:34, courseId: "274202", date: "2020-10-08",isRecord: false},
	{recordId:35, courseId: "274202", date: "2020-10-15",isRecord: false},
	{recordId:36, courseId: "274202", date: "2020-10-22",isRecord: false},

　　{recordId:37, courseId: "274250", date: "2020-10-02",isRecord: false},
	{recordId:38, courseId: "274250", date: "2020-10-09",isRecord: false},
	{recordId:39, courseId: "274250", date: "2020-10-16",isRecord: false},
	{recordId:40, courseId: "274250", date: "2020-10-23",isRecord: false},

　　{recordId:41, courseId: "274340", date: "2020-11-02",isRecord: false},
	{recordId:42, courseId: "274340", date: "2020-11-09",isRecord: false},
	{recordId:43, courseId: "274340", date: "2020-11-16",isRecord: false},
	{recordId:44, courseId: "274340", date: "2020-11-23",isRecord: false}
]);

//建立學生課程關聯資料
db.studentCourse.drop();

db.studentCourse.createIndex({studentId: 1, courseId: 1}, {unique: true});

db.studentCourse.insertMany([
    {studentId: '106820053', courseId: '268986', score: -1, attendance: 0},
    {studentId: '106820054', courseId: '268986', score: -1, attendance: 0},
    {studentId: '106820055', courseId: '268986', score: -1, attendance: 0},
    {studentId: '106820051', courseId: '268987', score: -1, attendance: 0},
    {studentId: '106820052', courseId: '268987', score: -1, attendance: 0},
    {studentId: '106820053', courseId: '268987', score: -1, attendance: 0},
    {studentId: '106820054', courseId: '268987', score: -1, attendance: 0},
    {studentId: '106820051', courseId: '269469', score: -1, attendance: 0},
    {studentId: '106820052', courseId: '269469', score: -1, attendance: 0},
    {studentId: '106820053', courseId: '269469', score: -1, attendance: 0},
    {studentId: '106820055', courseId: '269469', score: -1, attendance: 0}
]);

//建立學生課程關聯資料
db.user.drop();


db.user.insertMany([

	{email: "b", password: "b", salt: "sha512", name: "陳小一", _id: '106820051', identification: "student"},
	
	{email: "c", password: "c", salt: "sha512", name: "王小二", _id: '106820052', identification: "student"},
	
	{email: "d", password: "d", salt: "sha512", name: "李小三", _id: '106820053', identification: "student"},
	
	{email: "e", password: "e", salt: "sha512", name: "許小四", _id: '106820054', identification: "student"},
	
	{email: "f", password: "f", salt: "sha512", name: "鄭小五", _id: '106820055', identification: "student"},
	
	{email: "a1", password: "a1", salt: "sha512", name: "陳大一", _id: '00001', identification: "teacher"},
	
	{email: "a2", password: "a2", salt: "sha512", name: "王大二", _id: '00002', identification: "teacher"},
	
	{email: "a3", password: "a3", salt: "sha512", name: "李大三", _id: '00003', identification: "teacher"},
	
	{email: "a4", password: "a4", salt: "sha512", name: "許大四", _id: '00004', identification: "teacher"},
	
	{email: "a5", password: "a5", salt: "sha512", name: "鄭大五", _id: '00005', identification: "teacher"},

]);

db.attendance.drop();

db.attendance.insertMany([
	{studentId: "106820053", date: "2020-09-07", courseId: "268986", attendance: "1"},
]);

