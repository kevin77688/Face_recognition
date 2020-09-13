use nodejsTest
//�إ߾ǥ͸��
db.student.drop();
db.student.insertMany([ 
    //�����class�ҥH�Z�Ť@�˥��sfaculty
    {_id: 106820051, name: "���p�@", faculty: "�q��T", birthday: "1999/09/01", sex: "male", password: "000000", salt: "123456"},
    {_id: 106820052, name: "���p�G", faculty: "�q��T", birthday: "1999/09/02", sex: "female", password: "000000", salt: "123456"},
    {_id: 106820053, name: "���p�T", faculty: "�q��T", birthday: "1999/09/03", sex: "male", password: "000000", salt: "123456"},
    {_id: 106820054, name: "�\�p�|", faculty: "�q��T", birthday: "1999/09/04", sex: "female", password: "000000", salt: "123456"},
    {_id: 106820055, name: "�G�p��", faculty: "�q��T", birthday: "1999/09/05", sex: "male", password: "000000", salt: "123456"}
]); 
//�إ߱б¸��
db.professor.drop();
db.professor.insertMany([ 
    {_id: 00001, name: "���j�@", faculty: "��u�t", birthday: "1979/09/01", sex: "male", password: "000000", salt: "123456"},
    {_id: 00002, name: "���j�G", faculty: "��u�t", birthday: "1979/09/02", sex: "female", password: "000000", salt: "123456"},
    {_id: 00003, name: "���j�T", faculty: "��u�t", birthday: "1979/09/03", sex: "male", password: "000000", salt: "123456"},
    {_id: 00004, name: "�\�j�|", faculty: "��u�t", birthday: "1979/09/04", sex: "female", password: "000000", salt: "123456"},
    {_id: 00005, name: "�G�j��", faculty: "��u�t", birthday: "1979/09/05", sex: "male", password: "000000", salt: "123456"}
]);
//�إ߽ҵ{���
db.course.drop();
db.course.insertMany([
    {_id: 268986, teacherId: 00001, requiredOrElective: "elective", name: "�@�~�t��", englishName: "Operating Systems", code: 5903206, stage: 1, credits: 3},
    {_id: 268987, teacherId: 00001, requiredOrElective: "required", name: "��ȱM�D(�G)", englishName: "Special Project(II): 5903208", stage: 1, credits: 3},
    {_id: 269469, teacherId: 00002, requiredOrElective: "elective", name: "�]�Ⱥ޲z", englishName: "Finance Management", code: "5702007", stage: 1, credits: 3},
    {_id: 269481, teacherId: 00002, requiredOrElective: "required", name: "����ɦV�{���]�p���", englishName: "Object-Oriented Programming Labs", code: "5902209", stage: 1, credits: 2},
    {_id: 271547, teacherId: 00003, requiredOrElective: "required", name: "��|", englishName: "Physical Education", code: "1001001", stage: 6, credits: 0},
    {_id: 272986, teacherId: 00003, requiredOrElective: "elective", name: "�]�p�˦�", englishName: "Design Pattern", code: "5904322", stage: 1, credits: 3},
    {_id: 273941, teacherId: 00004, requiredOrElective: "required", name: "���z�]���v", englishName: "Intellectual Property", code: "1410145", stage: 1, credits: 2},
    {_id: 274128, teacherId: 00004, requiredOrElective: "required", name: "�H�u���z����", englishName: "Introduction to Artificial Intelligence", code: "1418015", stage: 1, credits: 2},
    {_id: 274202, teacherId: 00005, requiredOrElective: "elective", name: "�������ι��", lishName: "Cloud Applications Practice", code: "5902310", stage: 1, credits: 3},
    {_id: 274250, teacherId: 00005, requiredOrElective: "elective", name: "¾�P�i�����y", englishName: "Lecture: Increase capacity for career exploration", code: "AA04017", stage: 1, credits: 1},
    {_id: 274340, teacherId: 00005, requiredOrElective: "elective", name: "����[���i���y", englishName: "Feast for the global perspective", code: "AA01023", stage: 1, credits: 1},
]);
//�إ߾ǥͽҵ{���p���
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