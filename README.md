# HRKsCinema
This is the repository of my project for CGI's internship application. 

# How to get it running

## Install IntelliJ (I don't know if it also works on other IDE-s)

## Install PostgreSQL from https://www.postgresql.org/ (VERSION 15)
- Make the profile like this-  username: postgres, password: 0000
- The process is pretty self-explanatory, but you might encounter a window labeled "Stack builder".
- There choose PostgreSQL 15 ... on port 5432
- In the next window press "Cancel"

### Then you need to make the PostgreSQL a path variable:
Here are the instructions for Windows (on Mac it is easier):
- Locate the PostgreSQL Bin Directory: First, you need to find the directory where PostgreSQL is installed on your system. By default, on Windows, PostgreSQL is installed in C:\Program Files\PostgreSQL\<version>\bin (for Windows).
- Add PostgreSQL Bin Directory to PATH:
- Right-click on "This PC" or "Computer" (depending on your Windows version) and select "Properties".
- Click on "Advanced system settings" on the left sidebar.
- In the System Properties window, click on the "Environment Variables" button.
- In the Environment Variables window, under "System variables", find the "Path" variable and select it. Click on "Edit".
- Click on "New" and add the path to the PostgreSQL bin directory (e.g., C:\Program Files\PostgreSQL\<version>\bin (for Windows)).
- Click "OK" on all windows to save the changes.

With the installation of PostgreSQL you also get PgAdmin. Go there and create a new database named Cinema.

Download the database backup dump file from here: https://drive.google.com/file/d/1V4Cn8MzmEjxoZP3BWN9vEq5zjonSfUtr/view?usp=sharing

Then in your terminal type: psql -U postgres Cinema -f <path_to_the_downloaded_dump_file>

Then go to pgAdmin and refresh the Cinema database by right-click -> refresh

## Install node.js from https://nodejs.org/en/download

## Navigate to .../HRKsCinema/frontend in your terminal and type:
npm install

## To run the project you have to run the backend from IntelliJ and the frontend from .../HRKsCinema/frontend in your terminal by typing npm start



    
