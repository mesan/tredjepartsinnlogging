import 'file?name=[name].[ext]!./index.html'
import 'file?name=[name].[ext]!./core.css'
import 'babel-polyfill'
import Rx from 'rx'

import {userSubject, secretSubject, login, logout, saveSecret, verify} from './rest'
import contentHtml from 'html!./content.html'

document.body.innerHTML = contentHtml

const userDiv = document.querySelector(".user")
const loginDiv = document.querySelector(".login")
const secretDiv = document.querySelector(".secret")
const loggedInUserSpan = document.querySelector("#logged-in-user")

userSubject.subscribe(username => {
    loggedInUserSpan.innerHTML = username
    userDiv.style.display = 'block'
    secretDiv.style.display = 'block'
    loginDiv.style.display = 'none'
})


const usernameInput = document.querySelector("#username-input")
Rx.Observable.fromEvent(document.querySelector("#login-button"), 'click')
    .subscribe((event) => {
        event.preventDefault()
        login(usernameInput.value)
        usernameInput.value = ''
    })


const secretInput = document.querySelector('#secret-input')
secretSubject.subscribe(secret => secretInput.value = secret)
Rx.Observable.fromEvent(secretInput, 'keyup')
    .debounce(300)
    .distinctUntilChanged()
    .pluck('target', 'value')
    .subscribe(secret => saveSecret(secret))


Rx.Observable.fromEvent(document.querySelector('#logout-button'), 'click')
    .subscribe(() => {
        gapi.auth2.getAuthInstance().signOut().then(function () {
            logout()
            secretInput.value = ''
            userDiv.style.display = 'none'
            secretDiv.style.display = 'none'
            loginDiv.style.display = 'block'
        })
    })

window.onSignIn = function onSignIn(googleUser) {
    let token = googleUser.getAuthResponse().id_token;
    verify(token)
}