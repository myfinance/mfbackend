const BACKEND_URL = 'http://localhost:8181/dac/rest/marketdata/environments/dev/instruments';


/**
 * Loads a list of all greetings from the server
 * @param onSuccess {function} A Callback function that is invoked when a successful response is received from the server.
 * The callback is invoked with one parameter: an Array of all greetings
 * @param onFailure {function} The callback function is invoked when the server call failed. The callback is invoked
 * with one parameter: a string with an error message
 */
export const loadFromServer = (onSuccess, onFailure) => {
    const handleServerResponse = response => response.json()
        .then(json => response.status === 200 ? onSuccess(json) : onFailure(json.error));
    const handleServerError = err => onFailure(err.message);
    const handleUnexpectedError = err => onFailure('Unexpected error: ' + err);

    return fetch(BACKEND_URL, { credentials: 'include' })
        .then(handleServerResponse, handleServerError)
        .catch(handleUnexpectedError)
        ;
};