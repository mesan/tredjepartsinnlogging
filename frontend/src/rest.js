import Rx from 'rx'

function getUser() {
    fetch("/api/user", {credentials: 'include'})
        .then(res => res.status === 200
            ? res.text()
            : Promise.reject())
        .then(username => {
            userSubject.onNext(username)
            getSecret()
        })
        .catch(() => {
        })
}

function getSecret() {
    fetch('/api/secret', {credentials: 'include'})
        .then(res => res.status === 200
            ? res.text()
            : Promise.reject())
        .then(secret => secretSubject.onNext(secret))
        .catch(() => {
        })
}

getUser()

export const userSubject = new Rx.Subject()
export const secretSubject = new Rx.Subject()

export const login = username =>
    fetch('/api/login', {credentials: 'include', method: 'POST', body: username})
        .then(res => {
            if (res.status === 204)
                getUser()
        })

export const saveSecret = secret =>
    fetch('/api/secret', {credentials: 'include', method: 'PUT', body: secret})

export const logout = () => {
    document.cookie = 'JSESSIONID=; expires=Thu, 01 Jan 1970 00:00:01 GMT;'
    fetch('/api/logout', {credentials: 'include', method: 'POST'})
}

export const verify = token =>
    fetch('/api/verify', {credentials: 'include', method: 'POST', body: token})
        .then(res => {
            if (res.status === 204)
                getUser()
        })
