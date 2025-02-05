If your appletviewer is blank when you run the view.html file, it has something to do with your file permissions. 
\nIf everything works, great!
\nIf it doesn't, download the java.policy into your directory
\nand use this command: appletviewer -J-Djava.security.policy=java.policy view.html
