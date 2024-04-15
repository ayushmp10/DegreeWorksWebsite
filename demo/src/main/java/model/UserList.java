package model;

import java.util.ArrayList;

public class UserList {
    private static UserList userList;
    private ArrayList<User> users;

    private UserList() {
        this.users = new ArrayList<>();
    }

    public static UserList getInstance() {
        if (userList == null) {
            userList = new UserList();
        }
        return userList;
    }

    public void addUser(User user) {
        users.add(user);
    }

    // public void addUser(Student student){
    // users.add(student);
    // }
    // public void addUser(Guardian guardian){
    // users.add(guardian);
    // }
    public User getUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().toLowerCase().equals(username.toLowerCase())
                    && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // public void saveUsers(){
    // DataWriter.saveUsers(userList.getUsers());
    // }

    // public void loadUsers(){
    // setUsers(DataLoader.getUsers());
    // }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public ArrayList<Student> getStudents() {
        ArrayList<Student> returnStudents = new ArrayList<Student>();
        for (User user : users) {
            if (user instanceof Student) {
                returnStudents.add((Student) user);
            }
        }
        return returnStudents;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public boolean isValidUser(String username, String password) {
        /*
        for(User user : users){
            if(user.getUsername().toLowerCase().equals(username) && user.getPassword().equals(password)){
                return true;
            }
        }
        return false;
        */
        return true;
    }

    public ArrayList<Advisor> getAdvisors() {
        ArrayList<Advisor> returnAdvisors = new ArrayList<Advisor>();
        for (User user : users) {
            if (user instanceof Advisor) {
                returnAdvisors.add((Advisor) user);
            }
        }
        return returnAdvisors;
    }

    public ArrayList<Guardian> getGuardians() {
        ArrayList<Guardian> returnGuardians = new ArrayList<Guardian>();
        for (User user : users) {
            if (user instanceof Guardian) {
                returnGuardians.add((Guardian) user);
            }
        }
        return returnGuardians;
    }

}