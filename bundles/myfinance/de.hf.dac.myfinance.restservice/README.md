#Content

This Project contains the rest-api for MyFinance

#Here are some coding conventions for Rest:

* When should you use Path Variable, and how about Query Parameter?
If you want to identify a resource, you should use Path Variable. But if you want to sort or filter items, then you should use query parameter.
/users # Fetch a list of users
/users?occupation=programer # Fetch a list of programer user
/users/123 # Fetch a user who has id 123