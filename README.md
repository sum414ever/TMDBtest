# TMDBtest

Write the application that allows user to review the list of ongoing movies and add them to favorites. You should provide your solution as a link to public repository on any git hosting service you prefer: GitHub, GitLab, Bitbucket. Please make commits of logical units, so we can evaluate your progress and add comments as you see fit.

Functional requirements:

The main screen contains two tabs: All and Favorites.
Tab All should display a list of ongoing movies with pagination.
Tab Favorites should display a list of bookmarked movies
User can add / remove movie to / from his favorites. All changes should be visible immediately on both tabs
User can share movie info via any existing provider
User can refresh movies list using pull-to-refresh.
User can see early loaded content without internet connection (only 1st page, without pagination)
Screen UI should display one of the following states:

Loading
Refreshing
Loading More (pagination)
Error
Content (just showing movies)

Optional Requirements
Implement sign-in with Facebook and show user avatar

Technical requirements
Kotlin is required
Sqlite / Room is required
MVP / MVVM / MVI architecture is required;
Clean Architecture patterns are mandatory;
Dependency injection is required;

Notes
Your code should be testable, scalable and flexible
The most important parts of logic should be covered with unit tests (you are free to decide what parts)
You are free to use any libraries
You are free to implement your own design
