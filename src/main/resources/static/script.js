const BACKEND_URL = 'http://localhost:8080';

function process_form_data(form_id) {
    const form = document.getElementById(form_id);
    const form_data = new FormData(form);

    if (form_id === "movie-selection-form") {
        const selected_movie = document.querySelector('input[name="movie"]:checked');
        if (selected_movie !== null) {
            const chosen_movie_id = selected_movie.value;
            return chosen_movie_id
        } else {
            return null;
        }

    }
}

function choose_seats() {
    const movie_id = process_form_data("movie-selection-form");
    const ticket_count = document.getElementById("ticket-selection").value;
    console.log("here");
    console.log(typeof movie_id);
    if (movie_id !== null) {
        console.log(movie_id + "  " + ticket_count);
        // window.location.href = `./loading.html?url=${url}&media_type=${media_type}&converted_from=${converted_from}`;
        // if (post_selected_movie(movie_id) === true) {
        //    console.log("success")
        // }
    }
}

async function post_selected_movie(movie_id){
    const url = BACKEND_URL + '/movies/selected' + '?id=' + movie_id;
    const response = await fetch(url,  {
        method: 'POST',
    });
    return response.status === 200;
}

async function load_movie_data() {

    const movies_selection_field = document.getElementById('movie-selection-box');
    const url = BACKEND_URL + '/movies';
    const response = await fetch(url, {
        method: 'GET',
    });

    if (response.status !== 200) {
        movies_selection_field.innerText = response.status.toString();
    } else {
        const data = await response.json();

        // create form with radio buttons
        let displayed_movies = "";
        for (const movie of data) {
            const displayed_label = movie.asString;
            displayed_movies +=
                `<div>
                    <input type="radio" class="media" name="movie" value="${movie.id}"> ${displayed_label}</input>
                </div>`
        }
        movies_selection_field.innerHTML = displayed_movies;
    }
}


