# Cinema application

Simple lightweight cinema application that lets the user browse movies and get recommendations.
After selecting the movie and buying the tickets, the user is given recommendations regarding the seating plan.
Recommended seats are all next to each other and as close to the centre of the cinema hall as possible.

Current main branch contains the refactored and restructured version of the test assignment.
Original submission, which earned me my first internship in summer 2024, is available [here](https://github.com/Rsand03/cinema-application/tree/original-submission).


## Prerequisites

Before running the application, ensure you have the following installed:

- [Java JDK 21+](https://adoptopenjdk.net/)
- [Gradle](https://gradle.org/) (if building manually)


## Installation and setup

1) Clone this repo
In your terminal run:
```
git clone [https://github.com/Rsand03/cinema-application.git]
```
2) Open the project in your IDE (preferably IntelliJ IDEA):
3) Run CinemaApplication.java
4) Navigate to http://localhost:8080/ in your browser


## Usage

On the homepage, all movie sessions are initially displayed to the user. Each session includes the movie title, genre, age rating, start time, and language.

### Sorting movies
At the top of the webpage, there are dropdown menus. The user can select the criteria by which movie sessions are sorted. Movies are not filtered based on a specific category if no parameter is selected. To apply the filter, the user must click the "Apply filter" button on the right. The appropriate movies will then appear on the screen, or an error message will be displayed if no movies are found. To return to the original menu, the user must click the "All movies" button.

### Movie recommendations
To receive movie session recommendations, the user must click the "Recommended movies" button. If the user has no viewing history (i.e., no tickets have been purchased yet), an error message will be displayed. Movies are recommended based on the genres of previously watched films. Sessions of already watched movies are not recommended, even if the session's language or start time is different.

### Purchasing tickets
To purchase a ticket, the user must select the desired movie session (by clicking on it) and choose the number of tickets to buy. It is possible to purchase between 1 and 5 tickets, with the default quantity set to 1. To complete the purchase, the user must click the green "BUY" button. If no movie has been selected, an error message will be displayed.

### Seating plan
After clicking the "BUY" button, the user is directed to the seating plan of the theater hall. The assigned seats are marked in green. If multiple tickets are purchased, the system prioritizes seats that are all next to each other and in the same row, preferably near the center of the hall. If, for example, 5 tickets are selected but no five adjacent free seats are available, any available seats as close to the center as possible will be chosen. In this case, a corresponding notification will also be displayed. The "Back" button returns the user to the movie selection page (the previously selected movie is added to the user's viewing history).

## Some examples of the front-end

![Alt text](https://github.com/Rsand03/cinema-application/blob/main/src/main/resources/assets/cinema-example.png)

![Alt text](https://github.com/Rsand03/cinema-application/blob/main/src/main/resources/assets/cinema-example-2.png)

## Contributing

RSand03
