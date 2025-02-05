If your appletviewer is blank when you run the view.html file, it has something to do with your file permissions.  
If everything works, great!  
If it doesn't, download the java.policy into your directory  
and use this command: appletviewer -J-Djava.security.policy=java.policy view.html  
