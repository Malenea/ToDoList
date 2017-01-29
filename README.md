# ToDoList

This TodoList project is meant to provide a simple yet fully functional TodoList to the user.

## Getting Started

### Launching the app

![alt tag](http://i.imgur.com/SQQ7XPA.jpg)

Start by launching the application by clicking on the todolist button on your phone.
That should get you to the main menu of the application (as shown above).

![alt tag](http://i.imgur.com/i9EUOVk.jpg)

Now that you are on the main screen of the ToDoList application you can interact with it
in several ways:

1. - This is the title, pressing once on it will reverse the display order or swype it back
to it's default. By default, the tasks are listed from the latest date to the oldest one.
You can know wether it's in default mode or not looking at the title, in default mode, the
title is not reversed.
2. - This button will allow you to create a new task by defining it's title.
cf Adding a task - Section.
3. - This is the list of tasks. If it's empty a text will be displayed in red on the bottom
notifying you that it's empty.
Else, the tasks will be organized in clickables bars displaying the title of the task,
the due date and it's status (wether it's done, to-be-done, or unknown) and it's category
(unknown, work, personal, errands or hobbies).
Clicking on the task will allow you to display further informations regarding it, such as it's
description, the due time...
4. - This button is part of the sorting options, it allows you to show tasks regarding their
statuses. For example, you could show only the done tasks or the to-be-done tasks in the list.
5. - This button is the other part of the sorting options, it allows to show tasks regarding
their categories. For example, you could show all the tasks that are part of the work category,
or the errands category.
4. & 5. - Those two buttons can be combined to display even more precise tasks, allowing one to
list all the stats corresponding to a certain status and belonging to a certain category.
For example list the to-be-done tasks from the hobbies category.

### Adding a task

Once you've clicked on a task, a more detailed screen about the task will pop-up in a
window as shown below.

![alt tag](http://i.imgur.com/wrN5OPz.jpg)

1. - This button is the status of the current task. To change it, just click on it until you
find the status you want to apply to the task.
The statuses show by order:
   * Unknown means the task has no status set.
   * Done means the task is completed.
   * To-be-done means the task is yet to be done.
2. - This button is the category of the current task. To change it, just click on it until you
find the category you want it to belong to.
The categories show by order:
   * Unknown means the task has no category to wich it belongs.
   * Work means the task belongs to the work category.
   * Perso means the task belongs to the personal category.
   * Errands means the task belongs to the errands category.
   * Hobbies means the task belongs to the hobbies category.
3. - This is the description of your task, upon creation, this information is set to default,
meaning it is set to "No note associated to this todo task yet.".
To modify it, just click on it and write your own description.
4. - This is the back button, pressing it results on going back to the list of tasks.
5. - This button allows you to add your task to the default android calendar. On the first use,
the application will ask you for authorization to access to then calendar. If you deny it,
the application won't be able to add the task to your calendar.
6. - This button allows you to delete a task. Upon deletion, any events synced to the default
calendar will be removed and you will be taken back to the list of tasks.


### Delete a task

Upon selecting to delete a task, the task will disappear and any event linked to this task will
also be removed from the default android calendar and the database.
Deletion is permanent and cannot be recovered.

### Edit a task

On the task screen, you can also click on the date (by default it will show "No date set yet."),
or the start time or the end time.
Upon clicking, a new window will appear allowing you to change the date or time (see below).

![alt tag](http://i.imgur.com/3kfIGvj.jpg) ![alt tag](http://i.imgur.com/8Bybvf8.jpg)

On the task screen, you can also modify the task's title by simply clicking on it, wich will
pop a new window to edit the title.

![alt tag](http://i.imgur.com/qFGQr77.jpg)

### Conception

This is a simple todo task list relying on the default Android SQLite Database handled by
a java class and displaying informations on a RecycleView using a custom adapter to populate it.


 Overview :
     *-----------------*  request *---------------*            *---------------*
     |                 |<---------*               | task list  |               |
     | SQLite Database *          |  DB Handler   *----------->* Main Activity |
     |                 |--------->*               |            |               |
     *-----------------*  query   *-------*-------*            *-------*-------*
                                      ^   '          Update            |
                                      |   *- - - - - - -*--------------*
                                      |                 |
                                      |                 v
                                      |        *--------*--------*
                                      |        |                 |      *------------------*
                                      |        | Custom Adapter  |      |                  |
                    SQLite DB Request |        |                 |      | Android Calendar |
                                      |        *--------*--------*      |                  |
                                      |                 |               *--------*---------*
                                      |                 v                        |
                                      |    *------------*-----------*            |
                                      |    |                        |            |
                                      |    | Layout / Recycler View |            | Calendar add
                             *--------*    |                        |            |
                             |        |    *--*------------------*--*            |
                             |        |       |                  |               |
                             |        |       v                  v               |
                             |  *-----*-------*---*          *---*-------------* |
                             |  |                 |          |                 | |
                             |  | Create / sort   |          | Task Activity   *-*
                             |  |                 |          |                 |
                             |  *-----------------*          *-*------*--------*
                             |                          Delete |      | Edit
                             *---------------------------------*------*

## Build with

* [Android Studio](https://developer.android.com/studio/index.html) - Android Framework
* [Gradle](https://gradle.org) - Build tool

## Authors

* **Olivier Conan** - *Initial work* - [Malenea](https://github.com/Malenea)