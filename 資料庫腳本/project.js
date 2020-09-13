use nodejsTest
//建立學生資料
db.student.drop();
db.student.insertMany([ 
    //不能取class所以班級一樣先叫faculty
    {_id: 106820051, name: "陳小一", faculty: "電資三", birthday: "1999/09/01", sex: "male", password: "000000", salt: "123456"},
    {_id: 106820052, name: "王小二", faculty: "電資三", birthday: "1999/09/02", sex: "female", password: "000000", salt: "123456"},
    {_id: 106820053, name: "李小三", faculty: "電資三", birthday: "1999/09/03", sex: "male", password: "000000", salt: "123456"},
    {_id: 106820054, name: "許小四", faculty: "電資三", birthday: "1999/09/04", sex: "female", password: "000000", salt: "123456"},
    {_id: 106820055, name: "鄭小五", faculty: "電資三", birthday: "1999/09/05", sex: "male", password: "000000", salt: "123456"}
]); 
//建立教授資料
db.professor.drop();
db.professor.insertMany([ 
    {_id: 00001, name: "陳大一", faculty: "資工系", birthday: "1979/09/01", sex: "male", password: "000000", salt: "123456"},
    {_id: 00002, name: "王大二", faculty: "資工系", birthday: "1979/09/02", sex: "female", password: "000000", salt: "123456"},
    {_id: 00003, name: "李大三", faculty: "資工系", birthday: "1979/09/03", sex: "male", password: "000000", salt: "123456"},
    {_id: 00004, name: "許大四", faculty: "資工系", birthday: "1979/09/04", sex: "female", password: "000000", salt: "123456"},
    {_id: 00005, name: "鄭大五", faculty: "資工系", birthday: "1979/09/05", sex: "male", password: "000000", salt: "123456"}
]);
//建立課程資料
db.course.drop();
db.course.insertMany([
    {_id: 268986, teacherId: 00001, requiredOrElective: "elective", name: "作業系統", englishName: "Operating Systems", code: 5903206, stage: 1, credits: 3},
    {_id: 268987, teacherId: 00001, requiredOrElective: "required", name: "實務專題(二)", englishName: "Special Project(II): 5903208", stage: 1, credits: 3},
    {_id: 269469, teacherId: 00002, requiredOrElective: "elective", name: "財務管理", englishName: "Finance Management", code: "5702007", stage: 1, credits: 3},
    {_id: 269481, teacherId: 00002, requiredOrElective: "required", name: "物件導向程式設計實習", englishName: "Object-Oriented Programming Labs", code: "5902209", stage: 1, credits: 2},
    {_id: 271547, teacherId: 00003, requiredOrElective: "required", name: "體育", englishName: "Physical Education", code: "1001001", stage: 6, credits: 0},
    {_id: 272986, teacherId: 00003, requiredOrElective: "elective", name: "設計樣式", englishName: "Design Pattern", code: "5904322", stage: 1, credits: 3},
    {_id: 273941, teacherId: 00004, requiredOrElective: "required", name: "智慧財產權", englishName: "Intellectual Property", code: "1410145", stage: 1, credits: 2},
    {_id: 274128, teacherId: 00004, requiredOrElective: "required", name: "人工智慧概論", englishName: "Introduction to Artificial Intelligence", code: "1418015", stage: 1, credits: 2},
    {_id: 274202, teacherId: 00005, requiredOrElective: "elective", name: "雲端應用實務", lishName: "Cloud Applications Practice", code: "5902310", stage: 1, credits: 3},
    {_id: 274250, teacherId: 00005, requiredOrElective: "elective", name: "職涯進擊講座", englishName: "Lecture: Increase capacity for career exploration", code: "AA04017", stage: 1, credits: 1},
    {_id: 274340, teacherId: 00005, requiredOrElective: "elective", name: "國際觀培養講座", englishName: "Feast for the global perspective", code: "AA01023", stage: 1, credits: 1},
]);
//建立學生課程關聯資料
db.studentCourse.drop();
db.studentCourse.createIndex({studentId: 1, courseId: 1}, {unique: true});
db.studentCourse.insertMany([
    {studentId: 106820051, courseId: 268986, score: -1, attendance: 0},
    {studentId: 106820052, courseId: 268986, score: -1, attendance: 0},
    {studentId: 106820053, courseId: 268986, score: -1, attendance: 0},
    {studentId: 106820054, courseId: 268986, score: -1, attendance: 0},
    {studentId: 106820055, courseId: 268986, score: -1, attendance: 0},
    {studentId: 106820051, courseId: 268987, score: -1, attendance: 0},
    {studentId: 106820052, courseId: 268987, score: -1, attendance: 0},
    {studentId: 106820053, courseId: 268987, score: -1, attendance: 0},
    {studentId: 106820054, courseId: 268987, score: -1, attendance: 0},
    {studentId: 106820055, courseId: 268987, score: -1, attendance: 0},
    {studentId: 106820051, courseId: 269469, score: -1, attendance: 0},
    {studentId: 106820052, courseId: 269469, score: -1, attendance: 0},
    {studentId: 106820053, courseId: 269469, score: -1, attendance: 0},
    {studentId: 106820054, courseId: 269469, score: -1, attendance: 0},
    {studentId: 106820055, courseId: 269469, score: -1, attendance: 0},
]);