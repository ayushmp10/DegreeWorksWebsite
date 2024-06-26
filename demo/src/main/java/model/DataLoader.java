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

public class DataLoader extends DataConstants {
	//public static ArrayList<Student> allStudents = new ArrayList<>();

	public static ArrayList<User> loadUser() {
		CourseList courseList = CourseList.getInstance();
		DegreeList degreeList = DegreeList.getInstance();
		
		ArrayList<User> allUsers = new ArrayList<User>();
		
		// HashMap<UUID, Integer> advisorToStudentMap = new HashMap<>();
		String[] files = {ADVISOR_FILE_NAME, STUDENT_FILE_NAME, PARENT_FILE_NAME};
		for (String file : files) {
			try {
				FileReader fileReader = new FileReader(file);
				JSONArray readerJSON = (JSONArray) new JSONParser().parse(fileReader);
				for (int i = 0; i < readerJSON.size(); i++) {
					// read basic user information
					JSONObject userJSONObject = (JSONObject) readerJSON.get(i);
					UUID id = UUID.fromString((String) userJSONObject.get(USER_UUID));
					String firstName = (String) userJSONObject.get(USER_FIRST_NAME);
					String lastName = (String) userJSONObject.get(USER_LAST_NAME);
					String phoneNumber = (String) userJSONObject.get(USER_PHONE_NUMBER);
					String username = (String) userJSONObject.get(USER_USERNAME);
					String password = (String) userJSONObject.get(USER_PASSWORD);
					String userType = (String) userJSONObject.get(USER_TYPE);
					if (userType.equalsIgnoreCase("student")) {
						// assume god advisor is the first user in the list
						// load student information
						String yearClass = (String) userJSONObject.get(STUDENT_YEAR);
						UUID advisorUUID = UUID.fromString((String) userJSONObject.get(STUDENT_ADVISOR));
						UUID guardianUUID = UUID.randomUUID();
						String studentUSCID = (String) userJSONObject.get(STUDENT_USC_ID);
						// int advisorIndex = advisorToStudentMap.get(advisorUUID);
						Advisor advisor = (Advisor) allUsers.get(0);
						// Guardian guardian = (Guardian) allUsers.get(1);
						String completedCredits = (String) userJSONObject.get(STUDENT_COMPLETED_CREDITS);
						String totalCredits = (String) userJSONObject.get(STUDENT_TOTAL_CREDITS);
						Double gpa = (Double) userJSONObject.get(STUDENT_GPA);
						String applicationArea = (String) userJSONObject.get(STUDENT_APPLICATION_AREA);
						String notes = (String) userJSONObject.get(STUDENT_ADVISING_NOTES);
						
						UUID degreeUUID = UUID.fromString((String) userJSONObject.get(STUDENT_DEGREE_ID));
						Degree degree = degreeList.getDegree(degreeUUID);

						HashMap<Course, String> completedCourse = new HashMap<>();
						JSONObject completedCoursesObject = (JSONObject) userJSONObject.get(STUDENT_COMPLETED_COURSES);
						for (Object courseID : completedCoursesObject.keySet()) {
							UUID courseUUID = UUID.fromString((String) courseID);
							Course course = courseList.getCourse(courseUUID);
							String grade = (String) completedCoursesObject.get(courseID);
							completedCourse.put(course, grade);
						}

						JSONObject currSemesterObject = (JSONObject) userJSONObject.get(STUDENT_CURRENT_SEMESTER);
						String season = (String) currSemesterObject.get(SEMESTER_SEASON);
						int year = ((Long) currSemesterObject.get(SEMESTER_YEAR)).intValue();
						int creditLimit = ((Long) currSemesterObject.get(SEMESTER_LIMIT)).intValue();
						ArrayList<Course> courses = new ArrayList<>();
						JSONArray coursesJSONArray = (JSONArray) currSemesterObject.get(SEMESTER_COURSES);
						for (int j = 0; j < coursesJSONArray.size(); j++) {
							Course course = courseList.getCourse(UUID.fromString((String) coursesJSONArray.get(j)));
							courses.add(course);
						}
						Semester currSemester = new Semester(season, year, creditLimit, courses);

						ArrayList<Semester> allSemesters = new ArrayList<>();
						JSONArray allSemestersJSONArray = (JSONArray) userJSONObject.get(STUDENT_ALL_SEMESTERS);
						for (int j = 0; j < allSemestersJSONArray.size(); j++) {
							JSONObject allSemestersJSONObject = (JSONObject) allSemestersJSONArray.get(j);
							String tempSeason = (String) allSemestersJSONObject.get(SEMESTER_SEASON);
							int tempYear = ((Long) allSemestersJSONObject.get(SEMESTER_YEAR)).intValue();
							int tempCreditLimit = ((Long) allSemestersJSONObject.get(SEMESTER_LIMIT)).intValue();
							ArrayList<Course> tempCourses = new ArrayList<>();
							JSONArray tempCoursesJSONArray = (JSONArray) allSemestersJSONObject.get(SEMESTER_COURSES);
							for (int k = 0; k < tempCoursesJSONArray.size(); k++) {
								Course course = courseList.getCourse(UUID.fromString((String) tempCoursesJSONArray.get(k)));
								tempCourses.add(course);
							}
							Semester tempSemester = new Semester(tempSeason, tempYear, tempCreditLimit, tempCourses);
							allSemesters.add(tempSemester);
						}

						Student student = new Student(id, username, password, firstName, lastName, yearClass, degree, completedCredits,
														totalCredits, gpa, phoneNumber, advisorUUID, guardianUUID, studentUSCID, applicationArea,
														notes, completedCourse, currSemester, allSemesters);
						allUsers.add(student);
						// allStudents.add(student);
						advisor.addStudent(student);
					} 
					else if (userType.equalsIgnoreCase("advisor")) {
						// get advisor information
						String building = (String) userJSONObject.get(ADVISOR_BUILDING);
						String roomNumber = (String) userJSONObject.get(ADVISOR_ROOM_NUMBER);
						ArrayList<Student> students = new ArrayList<Student>();

						Advisor advisor = new Advisor(id, username, password, firstName, lastName, students, phoneNumber, building, roomNumber);

						allUsers.add(advisor);
						//advisorToStudentMap.put(advisor.getUUID(), allUsers.indexOf(advisor));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// for (User user : allUsers) {
		// 	System.out.println(user.getFirstName());
		// }
		return allUsers;
	}
	public static ArrayList<Advisor> getAdvisors() {
        ArrayList<Advisor> advisors = new ArrayList<>();
        try {
            FileReader reader = new FileReader(ADVISOR_FILE_NAME);
            JSONParser parser = new JSONParser();
            JSONArray advisorJSONArray = (JSONArray) new JSONParser().parse(reader);

            for (int i = 0; i < advisorJSONArray.size(); i++) {
                JSONObject advisorJSON = (JSONObject) advisorJSONArray.get(i);
                UUID id = UUID.fromString((String) advisorJSON.get(USER_UUID));
                String username = (String) advisorJSON.get(USER_USERNAME);
                String password = (String) advisorJSON.get(USER_PASSWORD);
                String firstName = (String) advisorJSON.get(USER_FIRST_NAME);
                String lastName = (String) advisorJSON.get(USER_LAST_NAME);
                String phoneNumber = (String) advisorJSON.get(USER_PHONE_NUMBER);
                String building = (String) advisorJSON.get(ADVISOR_BUILDING);
                String roomNumber = (String) advisorJSON.get(ADVISOR_ROOM_NUMBER);
                
                // You may need to handle the list of assigned students here
                
                Advisor advisor = new Advisor(id, username, password, firstName, lastName, advisorJSONArray, phoneNumber, building, roomNumber);
                advisors.add(advisor);
            }
            return advisors;
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
			System.out.println(courseJSONArray.size());
			for (int i = 0; i < courseJSONArray.size(); i++) {
				JSONObject courseJSON = (JSONObject) courseJSONArray.get(i);
				UUID id = UUID.fromString((String) courseJSON.get(COURSE_SET_UUID));
				String subject = (String) courseJSON.get(COURSE_SET_SUBJECT);
				String number = (String) courseJSON.get(COURSE_NUMBER);
				String name = (String) courseJSON.get(COURSE_NAME);
				String description = (String) courseJSON.get(COURSE_DESCRIPTION);
				int credits = ((Long) courseJSON.get(COURSE_CREDIT_HOURS)).intValue();
				// TODO check if this is a valid way of reading an array from the json file
				ArrayList<Season> semestersOffered = new ArrayList<Season>();
				JSONArray semesterJSON = (JSONArray) courseJSON.get(COURSE_SEMESTER_OFFERED);
				for (int j = 0; j < semesterJSON.size(); j++) {
					semestersOffered.add(Season.valueOf(((String) semesterJSON.get(j)).toUpperCase()));
				}
				// the array list type needs to be determined (don't know what object is used to
				// store the information in the scraped file)
				// temporarily we are saving it as course but it isn't that
				ArrayList<Prerequisites> prerequisites = new ArrayList<Prerequisites>();
				JSONArray prereqJSONArray = (JSONArray) courseJSON.get(COURSE_PREREQUISITES);
				// loading prerequisites
				// load all courses to queue and then load them to courses array list
				Course course = new Course(id, subject, number, name, description, credits,
						semestersOffered, prerequisites); // remove implementation for core categories and corequisites

				if (prereqJSONArray.size() == 0) {
					courses.add(course);
					int index = courses.indexOf(course);
					courseMap.put(course.getUUID(), index);
				} else {
					courseQueue.add(new Pair<Course, Object>(course, prereqJSONArray));
				}

				// ArrayList<String> corequisites =
				// (ArrayList<String>)courseJSON.get(COURSE_COREQUISITES);

				// ArrayList<String> courseCoreCategories =
				// (ArrayList<String>)courseJSON.get(COURSE_CORE_CATEGORIES);

				// courses.add(new Course(id, subject, number, name, description, credits,
				// semestersOffered, prerequisites, corequisites, courseCoreCategories));
			}
		} catch (Exception e) {
			e.printStackTrace(); // couldn't load the file
		}

		// empty out the queue - gets stuck in this loop infinitely
		while (!courseQueue.isEmpty()) {
			// get the first course in the queue which is tied to the prereq array
			Pair<Course, Object> coursePair = courseQueue.poll();
			Course course = coursePair.getKey();
			JSONArray prereqJSON = (JSONArray) coursePair.getValue();

			ArrayList<Prerequisites> allPrereqs = new ArrayList<Prerequisites>();
			boolean load = true;

			for (int i = 0; i < prereqJSON.size(); i++) {
				JSONObject prereqObject = (JSONObject) prereqJSON.get(i);
				int choices = ((Long) prereqObject.get(COURSE_PREREQUISITE_CHOICES)).intValue();
				String minGrade = (String) prereqObject.get(COURSE_PREREQUISITES_MIN_GRADE);

				ArrayList<Course> courseOptions = new ArrayList<Course>();
				JSONArray courseOptionArray = (JSONArray) prereqObject.get(COURSE_PREREQUISITES_COURSE_OPTION);
				// load all the prereq options
				//System.out.println("Prereq size " + courseOptionArray.size()); 
				for (int j = 0; j < courseOptionArray.size(); j++) {
					UUID courseID = UUID.fromString((String) courseOptionArray.get(j));
					if (courseMap.containsKey(courseID)) {
						courseOptions.add(courses.get(courseMap.get(courseID)));
						System.out.println("adding a course as an option");
					}
				}

				// are all the options loaded?
				if (courseOptions.size() == courseOptionArray.size()) {
					Prerequisites tempPrereq = new Prerequisites(choices, minGrade, courseOptions);
					allPrereqs.add(tempPrereq);
					
					// SO FAR SO GOOD STILL IS GETTING ALL THE PREREQS
				} else {
					load = false;
					// never gets here
					break;
				}

			}

			if (load) {
				courses.remove(course);
				course.setPrerequisites(allPrereqs);
				courses.add(course);
				courseMap.put(course.getUUID(), courses.indexOf(course));
			} else {
				courseQueue.add(coursePair);
			}
		}

		return courses;
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

				UUID degreeUUID = UUID.fromString((String)degreeObject.get(DEGREE_UUID));
				if (degreeObject.get(DEGREE_UUID) != null) {
					degreeUUID = UUID.fromString((String) degreeObject.get(DEGREE_UUID));
				}

				// need to make it a primitive type so that it can be casted
				int requiredCredits = ((Long)degreeObject.get(DEGREE_CREDITS)).intValue();
				if (degreeObject.get(DEGREE_TOTAL_REQ_CREDIT) != null) {
					requiredCredits = (int) ((long) degreeObject.get(DEGREE_TOTAL_REQ_CREDIT));
				}

				String subject = (String) degreeObject.get(DEGREE_SUBJECT_NAME);

				// get major courses with degree
				HashMap<Course, Integer> majorCourses = new HashMap<Course, Integer>();
				JSONObject majorCoursesObject = (JSONObject) degreeObject.get(DEGREE_MAJOR_COURSE);
				if (majorCoursesObject != null) {
					for (Object courseUUID : majorCoursesObject.keySet()) {
						UUID uuid = UUID.fromString((String) courseUUID);
						Course tempCourse = courseList.getCourse(uuid);
						// need to make it a primitive type so that it can be casted
						int prefSemester = (int) ((long) majorCoursesObject.get(courseUUID));
						majorCourses.put(tempCourse, prefSemester);
					}
				}

				// load all electives
				ArrayList<Elective> electiveList = new ArrayList<Elective>();
				JSONArray electiveArray = (JSONArray) degreeObject.get(DEGREE_ELECTIVES);
				if (electiveArray != null) {
					for (int j = 0; j < electiveArray.size(); j++) {
						JSONObject electivesObject = (JSONObject) electiveArray.get(j);
						String type = (String) electivesObject.get(ELECTIVE_TYPE);
						int electiveCreditReq = (int) ((long) electivesObject.get(ELECTIVE_CREDIT_REQ));
						JSONObject courseObject = (JSONObject) electivesObject.get(ELECTIVE_COURSE_OPTIONS);
						HashMap<Course, Integer> courseOptions = new HashMap<Course, Integer>();
						// load the electives and add them to the options
						for (Object courseUUID : courseObject.keySet()) {
							UUID uuid = UUID.fromString((String) courseUUID);
							Course tempCourse = courseList.getCourse(uuid);
							int prefSemester = (int) ((long) courseObject.get(courseUUID));
							courseOptions.put(tempCourse, prefSemester);
						}
						electiveList.add(new Elective(type, electiveCreditReq, courseOptions));
					}
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
