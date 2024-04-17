package model;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;;

public class DataLoader extends DataConstants{
	private static CourseList courseList = CourseList.getInstance();
	private static DegreeList degreeList = DegreeList.getInstance();
	private static UserList userList = UserList.getInstance();
	public static ArrayList<Student> loadStudents() {
		ArrayList<Student> students = new ArrayList<Student>();
		try {
			FileReader reader = new FileReader(STUDENT_FILE_NAME);
			JSONParser parser = new JSONParser();	
			JSONArray studentJSONArray = (JSONArray)new JSONParser().parse(reader);
			
			for(int i=0; i < studentJSONArray.size(); i++) {
				JSONObject studentJSON = (JSONObject)studentJSONArray.get(i);
				UUID id = UUID.fromString((String)studentJSON.get(USER_UUID));
				String username = (String)studentJSON.get(USER_USERNAME);
				String password = (String)studentJSON.get(USER_PASSWORD);
				String firstName = (String)studentJSON.get(USER_FIRST_NAME);
				String lastName = (String)studentJSON.get(USER_LAST_NAME);
				String phoneNumber = (String)studentJSON.get(USER_PHONE_NUMBER);
				String gradeYear = (String)studentJSON.get(STUDENT_YEAR);
				String uscID = (String)studentJSON.get(STUDENT_USC_ID);
				// String major = (String)studentJSON.get(STUDENT_MAJOR);
				// ArrayList<String> completedCourses = (ArrayList<String>)studentJSON.get(STUDENT_COMPLETED_COURSES);
				Long completedCredits = (Long)studentJSON.get(STUDENT_COMPLETED_CREDITS);
				Long totalCredits = (Long)studentJSON.get(STUDENT_TOTAL_CREDITS);
				Long gpa = (Long)studentJSON.get(STUDENT_GPA);
				UUID advisorId = UUID.fromString((String)studentJSON.get(STUDENT_ADVISOR));
				// TODO create an advisor based on the uuid given
				UUID guardianId = UUID.fromString((String)studentJSON.get(STUDENT_GUARDIAN));
				// TODO create a guardian based on the uuid given
				String applicationArea = (String)studentJSON.get(STUDENT_APPLICATION_AREA);
				String advisorNotes = (String)studentJSON.get(STUDENT_ADVISING_NOTES);
				// need to figure out how completed courses are stored with their grades TODO update student class
				UUID degreeUUID = UUID.fromString((String) studentJSON.get(STUDENT_DEGREE_ID));
				Degree degree = degreeList.getDegree(degreeUUID);
				
				HashMap<Course, String> completedCourses = new HashMap<Course, String>();
				JSONObject completedCourseObject = (JSONObject) studentJSON.get(STUDENT_COMPLETED_COURSES);
				for (Object key : completedCourseObject.keySet()) {
					UUID courseUUID = UUID.fromString((String) key);
					Course course = courseList.getCourse(courseUUID);
					String grade = (String) completedCourseObject.get(key);
					completedCourses.put(course, grade);
				}

				JSONObject currSemesterObject = (JSONObject) studentJSON.get(STUDENT_CURRENT_SEMESTER);
				String season = (String) currSemesterObject.get(SEMESTER_SEASON);
				int year = ((Long) currSemesterObject.get(SEMESTER_YEAR)).intValue();
				int limit = ((Long) currSemesterObject.get(SEMESTER_LIMIT)).intValue();
				ArrayList<Course> courses = new ArrayList<Course>();
				JSONArray courseArray = (JSONArray) currSemesterObject.get(SEMESTER_COURSES);
				for (int j = 0; j < courseArray.size(); j++) {
					Course course = courseList.getCourse(UUID.fromString((String) courseArray.get(j)));
					courses.add(course);
				}
				Semester currSemester = new Semester(season, year, limit, courses);

				ArrayList<Semester> allSemesters = new ArrayList<Semester>();
				JSONArray allSemesterArray = (JSONArray) studentJSON.get(STUDENT_ALL_SEMESTERS);
				for (int j = 0; j < allSemesterArray.size(); j++) {
					JSONObject allSemesterObject = (JSONObject) allSemesterArray.get(j);
					String tempSeason = (String) allSemesterObject.get(SEMESTER_SEASON);
					int tempYear = ((Long) allSemesterObject.get(SEMESTER_YEAR)).intValue();
					int tempCreditLimit = ((Long) allSemesterObject.get(SEMESTER_LIMIT)).intValue();
					ArrayList<Course> tempCourses = new ArrayList<Course>();
					JSONArray tempCourseArray = (JSONArray) allSemesterObject.get(SEMESTER_COURSES);
					for (int a = 0; a < tempCourseArray.size(); a++) {
						Course course = courseList.getCourse(UUID.fromString((String) tempCourseArray.get(j)));
						courses.add(course);
					}
					Semester tempSemester = new Semester(tempSeason, tempYear, tempCreditLimit, tempCourses);
					allSemesters.add(tempSemester);
				}
				students.add(new Student(id, username, password, firstName, lastName, gradeYear, degree, completedCredits,
										totalCredits, gpa, phoneNumber, advisorId, guardianId, uscID, applicationArea, advisorNotes,
										completedCourses, currSemester, allSemesters));
			}
			
			return students;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static ArrayList<Advisor> getAdvisors() {
		ArrayList<Advisor> advisors = new ArrayList<Advisor>();
		try {
			FileReader reader = new FileReader(ADVISOR_FILE_NAME);
			JSONParser parser = new JSONParser();
			JSONArray advisorJSONArray = (JSONArray)new JSONParser().parse(reader);

			for (int i = 0; i < advisorJSONArray.size(); i++) {
				JSONObject advisorJSON = (JSONObject)advisorJSONArray.get(i);
				UUID id = UUID.fromString((String)advisorJSON.get(USER_UUID));
				String username = (String)advisorJSON.get(USER_USERNAME);
				String password = (String)advisorJSON.get(USER_PASSWORD);
				String firstName = (String)advisorJSON.get(USER_FIRST_NAME);
				String lastName = (String)advisorJSON.get(USER_LAST_NAME);
				// figure out how to store all the stuednts the advisor has
				// the json file will store them with their uuids
				// needs to be converted to students instead of uuids
				// 	or keep it as uuids and get the data when required 
				// 	to do this would need to create a getStudent method where the JSON file is searched for the specific UUID
				String phoneNumber = (String)advisorJSON.get(USER_PHONE_NUMBER);
				String building = (String)advisorJSON.get(ADVISOR_BUILDING);
				String roomNumber = (String)advisorJSON.get(ADVISOR_ROOM_NUMBER);
				ArrayList<Student> students = new ArrayList<Student>();
				JSONArray advisorStudents = (JSONArray) advisorJSON.get(ADVISOR_ASSIGNED_STUDENTS);
				// get all students in the system
				ArrayList<Student> allStudents = loadStudents();
				for (int j = 0; j < advisorStudents.size(); j++) {
					JSONObject studentObject = (JSONObject) advisorStudents.get(j);
					UUID uuid = UUID.fromString((String) studentObject.get(USER_UUID));
					// instead of reading in all of the information from the object
					// just check all students for the same uuid
					for (Student student : allStudents) {
						if (student.getID().equals(uuid)) {
							students.add(student);
						}
					}
					// above is a tedious check but logically should work
				}
				advisors.add(new Advisor(id, username, password, firstName, lastName, students, phoneNumber, building, roomNumber));
			}
			return advisors;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// Don't need this for scenarios 1 and 2 can implement later
	public static ArrayList<Guardian> getGuardians() {
		ArrayList<Guardian> guardians = new ArrayList<Guardian>();

		try {
			FileReader reader = new FileReader(PARENT_FILE_NAME);
			JSONParser parser = new JSONParser();
			JSONArray guardianJSONArray = (JSONArray)new JSONParser().parse(reader);

			for (int i = 0; i < guardianJSONArray.size(); i++) {
				JSONObject guardianJSON = (JSONObject)guardianJSONArray.get(i);
				UUID id = UUID.fromString((String)guardianJSON.get(USER_UUID));
				String username = (String)guardianJSON.get(USER_USERNAME);
				String password = (String)guardianJSON.get(USER_PASSWORD);
				String firstName = (String)guardianJSON.get(USER_FIRST_NAME);
				String lastName = (String)guardianJSON.get(USER_LAST_NAME);
				// figure out how to store all the stuednts the advisor has
				// the json file will store them with their uuids
				// needs to be converted to students instead of uuids
				// 	or keep it as uuids and get the data when required 
				// 	to do this would need to create a getStudent method where the JSON file is searched for the specific UUID
				UUID studentUUID = UUID.fromString((String)guardianJSON.get(PARENT_CHILDREN));
				ArrayList<Student> allStudents = userList.getStudents();
				Student guardianStudent = null;
				for (Student student : allStudents) {
					if (student.getID().equals(studentUUID)) {
						guardianStudent = student;
					}
				}
				String phoneNumber = (String)guardianJSON.get(USER_PHONE_NUMBER);
				guardians.add(new Guardian(id, username, password, firstName, lastName, phoneNumber, guardianStudent, true));
			}
			return guardians;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static ArrayList<Course> getCourses() {
		ArrayList<Course> courses = new ArrayList<Course>();
		HashMap<UUID, Integer> courseMap = new HashMap<>();
		Queue<Pair<Course, Object>> courseQueue = new LinkedList<>();

		try {
			FileReader reader = new FileReader(COURSE_FILE_NAME);
			JSONParser parser = new JSONParser();
			JSONArray courseJSONArray = (JSONArray) new JSONParser().parse(reader);

			for (int i = 0; i < courseJSONArray.size(); i++) {
				JSONObject courseJSON = (JSONObject)courseJSONArray.get(i);
				UUID id = UUID.fromString((String)courseJSON.get(COURSE_SET_UUID));
				String subject = (String)courseJSON.get(COURSE_SET_SUBJECT);
				String number = (String)courseJSON.get(COURSE_NUMBER);
				String name = (String)courseJSON.get(COURSE_NAME);
				String description = (String)courseJSON.get(COURSE_DESCRIPTION);
				String credits = (String)courseJSON.get(COURSE_CREDIT_HOURS);
				// TODO check if this is a valid way of reading an array from the json file
				ArrayList<Season> semestersOffered = (ArrayList<Season>)courseJSON.get(COURSE_SEMESTER_OFFERED);
				JSONArray semesterJSON = (JSONArray)courseJSON.get(COURSE_SEMESTER_OFFERED);
				for (int j = 0; j < semesterJSON.size(); j++) {
					semestersOffered.add(Season.valueOf(((String) semesterJSON.get(j)).toUpperCase()));
				}
				// the array list type needs to be determined (don't know what object is used to store the information in the scraped file)
				// temporarily we are saving it as course but it isn't that
				ArrayList<Prerequisites> prerequisites = new ArrayList<Prerequisites>();
				JSONArray prereqJSONArray = (JSONArray) courseJSON.get(COURSE_PREREQUISITES);
				// loading prerequisites
				// load all courses to queue and then load them to courses array list
				Course course = new Course(id, subject, number, name, description, credits, 
											semestersOffered, prerequisites); //  remove implementation for core categories and corequisites
				if (prereqJSONArray.size() == 0) {
					courses.add(course);
					int index = courses.indexOf(course);
					courseMap.put(course.getUUID(), index);
				} else {
					courseQueue.add(new Pair<Course, Object>(course, prereqJSONArray));
				}
				
				// ArrayList<String> corequisites = (ArrayList<String>)courseJSON.get(COURSE_COREQUISITES);

				// ArrayList<String> courseCoreCategories = (ArrayList<String>)courseJSON.get(COURSE_CORE_CATEGORIES);

				//courses.add(new Course(id, subject, number, name, description, credits, 
							 //semestersOffered, prerequisites, corequisites, courseCoreCategories));
			}
		}
		catch (Exception e) {
			e.printStackTrace(); // couldn't load the file
		}

		// empty out the queue
		while (!courseQueue.isEmpty()) {
			// get the first course in the queue which is tied to the prereq array
			Pair<Course, Object> coursePair = courseQueue.poll();
			Course course = coursePair.getKey();
			JSONArray prereqJSON = (JSONArray) coursePair.getValue();

			ArrayList<Prerequisites> allPrereqs = new ArrayList<Prerequisites>();
			boolean load = true;

			for (int i = 0; i < prereqJSON.size(); i++) {
				JSONObject prereqObject = (JSONObject) prereqJSON.get(i);
				int choices = ((Long) prereqObject.get(COURSE_PREREQUISITES)).intValue();
				String minGrade = (String) prereqObject.get(COURSE_PREREQUISITES_MIN_GRADE);

				ArrayList<Course> courseOptions = new ArrayList<Course>();
				JSONArray courseOptionArray = (JSONArray) prereqObject.get(COURSE_PREREQUISITES_COURSE_OPTION);
				// load all the prereq options
				for (int j = 0; j < courseOptionArray.size(); j++) {
					UUID courseID = UUID.fromString((String) courseOptionArray.get(i));
					if (courseMap.containsKey(courseID)) {
						courseOptions.add(courses.get(courseMap.get(courseID)));
					}
				}

				// are all the options loaded?
				if (courseOptions.size() == courseOptionArray.size()) {
					allPrereqs.add(new Prerequisites(choices, minGrade, courseOptions));
				} else {
					load = false;
					break;
				}

			}

			if (load) {
				course.setPrequisites(allPrereqs);
				courses.add(course);
				courseMap.put(course.getUUID(), courses.indexOf(course));
			} else {
				courseQueue.add(coursePair);
			}
		}

		
		return null;
	}
	private static class Pair<K, V> {
        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public String toString() {
            return "(" + key + ", " + value + ")";
        }
    }

	public static ArrayList<Degree> getDegrees() {
		CourseList courseList = CourseList.getInstance();
		ArrayList<Degree> degrees = new ArrayList<Degree>();

		try {
			FileReader reader = new FileReader(DEGREE_FILE_NAME);
			JSONArray degreeJSON = (JSONArray) new JSONParser().parse(reader);
			for (int i = 0; i < degreeJSON.size(); i++) {
				JSONObject degreeObject = (JSONObject) degreeJSON.get(i);
				// get basic degree information
				UUID degreeUUID = UUID.fromString((String) degreeObject.get(DEGREE_UUID));
				// need to make it a primitive type so that it can be casted
				int requiredCredits = (int) ((long) degreeObject.get(DEGREE_TOTAL_REQ_CREDIT));
				String subject = (String) degreeObject.get(DEGREE_SUBJECT_NAME);

				// get major courses with degree
				HashMap<Course, Integer> majorCourses = new HashMap<Course, Integer>(); 
				JSONObject majorCoursesObject = (JSONObject) degreeObject.get(DEGREE_MAJOR_COURSE);
				for (Object courseUUID : majorCoursesObject.keySet()) {
					UUID uuid = UUID.fromString((String) courseUUID);
					Course tempCourse = courseList.getCourse(uuid);
					// need to make it a primitive type so that it can be casted
					int prefSemester = (int) ((long) majorCoursesObject.get(courseUUID));
					majorCourses.put(tempCourse, prefSemester);
				}

				// load all electives
				ArrayList<Elective> electiveList = new ArrayList<Elective>();
				JSONArray electiveArray = (JSONArray) degreeObject.get(DEGREE_ELECTIVES);
				for (int j = 0; j < electiveArray.size(); j++) {
					JSONObject electivesObject = (JSONObject) electiveArray.get(j);
					String type = (String) electivesObject.get(ELECTIVE_TYPE);
					int electiveCreditReq = (int) ((long) electivesObject.get(ELECTIVE_CREDIT_REQ));
					JSONObject courseObject = (JSONObject) electivesObject.get(ELECTIVE_COURSE_OPTIONS);
					HashMap<Course, Integer> courseOptions = new HashMap<Course,Integer>();
					// load the electives and add them to the options
					for (Object courseUUID : courseObject.keySet()) {
						UUID uuid = UUID.fromString((String) courseUUID);
						Course tempCourse = courseList.getCourse(uuid);
						int prefSemester = (int) ((long) courseObject.get(courseUUID));
						courseOptions.put(tempCourse, prefSemester);
					}
					electiveList.add(new Elective(type, electiveCreditReq, courseOptions));
				}

				Degree degree = new Degree(degreeUUID, subject, subject, requiredCredits, majorCourses, electiveList);
				degrees.add(degree);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}

		return degrees;
	}
}
