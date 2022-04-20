package de.neuefische.smartcount;

//  This is a User Data Operations Object.
//  It is used to compute the due or surplus per person.

record UserDOO(String userName, double userDelta) {

    public UserDOO changeDelta(double newDelta) {
        return new UserDOO(userName, newDelta);
    }
}
