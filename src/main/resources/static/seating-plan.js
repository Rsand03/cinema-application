
const BACKEND_URL = 'http://localhost:8080/api';


/**
 * Requests seating plan data from back-end.
 * Displays notification in case no neighbouring seats are available or no seats are available at all.
 */
async function fetchSeatingPlan() {
    const params = new URLSearchParams(window.location.search);
    const ticketCount = params.get('ticket_count');

    const url = BACKEND_URL + "/seats" + "?ticketCount=" + ticketCount;
    const response = await fetch(url,  {
        method: 'GET'
    });

    const data = await response.json();
    // {"seatNumber": seat number, "occupationStatus": state of the seat (FREE / SELECTED / OCCUPIED)}
    if (response.status !== 200 && response.status !== 206) {
        displaySeatErrorMessage("No available seats.");
    } else if (response.status === 206) {
        displaySeatErrorMessage("Unfortunately no neighbouring seats were available.");
        renderSeatingPlan(data);
    } else {
        renderSeatingPlan(data);
    }
}


/**
 * Display error message on seating plan page.
 * @param {string} message_text text to display
 */
function displaySeatErrorMessage(message_text) {
    const errorMessageBox = document.getElementById("seats-error-message-box");
    errorMessageBox.innerText = message_text;
}


/**
 * Renders the seating plan based on seating plan data.
 * @param {list} seatingPlanData seating plan data in json format
 * seatingPlanData: {"seatNumber": seat number, "occupationStatus": state of the seat (FREE / SELECTED / OCCUPIED)}
 */
function renderSeatingPlan(seatingPlanData) {
    for (const seat of seatingPlanData) {
        const seatingPlanField = document.getElementById('seating-plan');
        // create form with radio buttons
        let displayedSeats = "";

        for (const seat of seatingPlanData) {
            const seatNumber = seat.seatNumber;
            let background_color;
            switch (seat.occupationStatus) {
                case "FREE": background_color = "lightgray"; break;
                case "OCCUPIED": background_color = "Red"; break;
                case "SELECTED": background_color = "Green"; break;
            }
            displayedSeats += `<div class="seat" style="background-color: ${background_color}">${seatNumber}</div>`
        }
        seatingPlanField.innerHTML = displayedSeats;
    }
}
