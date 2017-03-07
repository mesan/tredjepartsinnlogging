import 'file?name=[name].[ext]!./index.html'
import 'file?name=[name].[ext]!./core.css'
import 'babel-polyfill'

const h1 = document.createElement("h1");
h1.innerHTML = 'Det fungerer! :D';
document.body.appendChild(h1);

[...document.querySelector("h1")] .forEach(el => el.style.color = 'blue');

