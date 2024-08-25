# Simple Java Login Manager with an SQLite DB

## Features

- This system is intended as a simple login system with a registration area, login area, and a dashboard area.
- It makes use of a SQLite database that's created and initialised upon the first startup of the application.
- It utilizes some password protection within the main logic of the application, as when storing a password for a registered user, the password is hashed and a salt is generated and stored alonside the hashed password.
- As of now, all the input fields have some sort of error handling, to ensure the captured data is correctly stored.
- The dashboard has one label congratulating the logged in user for making it to second year, and wishing them luck, as well as a table to show all the registered users in the system(First and last names only), to give a sense of scale, will be removed in a later phase.

## Planned updates

- Password encryption using AES, alongside hashing the password before encryption for password safety.
- Better UI flow, and possibly some customizations to add dark mode.

## Design choices

- Made use of an SQLite database for concurency, rather than having a temporary user while the application is running, the user is represented as a permanent entity within the system.
- Complexity of the application has a certain art to it, how every single class works with the other to bring forth a GUI application that any user can interact with without having to be a developer themeselves.

## Potential errors

- None that I could find in my own testing.

## Reasons for creating this system

- This is my first formative assessment for my Java module.
