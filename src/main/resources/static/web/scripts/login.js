Vue.createApp({
  data() {
    return {
      firstNameR: "",
      lastNameR: "",
      emailR: "",
      passwordR: "",
      email: "",
      password: "",
    }
  },
  created() {

    axios.get('/api/clients')
      .then(datos => {


      })
      .catch(function (error) {
        if (error.response) {
          // The request was made and the server responded with a status code
          // that falls out of the range of 2xx
          console.log(error.response.data);
          console.log(error.response.status);
          console.log(error.response.headers);
        } else if (error.request) {
          // The request was made but no response was received
          // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
          // http.ClientRequest in node.js
          console.log(error.request);
        } else {
          // Something happened in setting up the request that triggered an Error
          console.log('Error', error.message);
        }
        console.log(error.config);
      });
  },
  methods: {
    formatMoney(amount) {
      return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD', minimumFractionDigits: 2 }).format(amount);
    },

    redirect(id) {
      let url = "/web/account.html?id=" + id;
      window.location.href(url);
      return 1;
    },

    signIn() {
      axios.post('/api/login', `email=${this.email}&password=${this.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
        .then(response => {
          console.log('signed in!!!');
          window.location.href = '/web/accounts.html'
        })
        .catch(function (error) {
          if (error.response) {
            // The request was made and the server responded with a status code
            // that falls out of the range of 2xx
            console.log(error.response.data);
            console.log(error.response.status);
            console.log(error.response.headers);
          } else if (error.request) {
            // The request was made but no response was received
            // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
            // http.ClientRequest in node.js
            console.log(error.request);
          } else {
            // Something happened in setting up the request that triggered an Error
            console.log('Error', error.message);
          }
          console.log(error.config);
        });
    },
    logOut() {
      axios.post('/api/logout')
        .then(response => {
          console.log('Log Out!!');
          window.location.href = '/web/index.html'
        })
    },
    hideOrShowPassword(){
      let password1,check;
    
      password1=document.getElementById("password1");
      check=document.getElementById("show");
    
      if(check.checked  )
      {
          password1.type = "text";
      }
      else 
      {
          password1.type = "password";
      }
    },
    register(){
      axios.post('/api/clients',`firstName=${this.firstNameR}&lastName=${this.lastNameR}&email=${this.emailR}&password=${this.passwordR}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        .then(response => {
          console.log('registered');
          this.password = this.passwordR;
          this.email = this.emailR;
          this.signIn();

        })
    }
  },
  computed: {

  }

}).mount('#app')

const $btnSignIn = document.querySelector('.sign-in-btn'),
  $btnSignUp = document.querySelector('.sign-up-btn'),
  $signUp = document.querySelector('.sign-up'),
  $signIn = document.querySelector('.sign-in');

document.addEventListener('click', e => {
  if (e.target === $btnSignIn || e.target === $btnSignUp) {
    $signIn.classList.toggle('active');
    $signUp.classList.toggle('active')
  }
});


// LOGIN 
document.querySelector('.img__btn').addEventListener('click', function() {
  document.querySelector('.cont').classList.toggle('s--signup');
});

// NAV BAR 
const body = document.querySelector("body"),
  sidebar = body.querySelector("nav"),
  toggle = body.querySelector(".toggle"),
  searchBtn = body.querySelector(".search-box"),
  modeSwitch = body.querySelector(".toggle-switch"),
  modeText = body.querySelector(".mode-text");

toggle.addEventListener("click", () => {
  sidebar.classList.toggle("close");
});

searchBtn.addEventListener("click", () => {
  sidebar.classList.remove("close");
});

modeSwitch.addEventListener("click", () => {
  body.classList.toggle("dark");

  if (body.classList.contains("dark")) {
    modeText.innerText = "Light mode";
  } else {
    modeText.innerText = "Dark mode";
  }
});

// // Image background login
// var index = 0;
// var listaimg = ["./pics/background/1.jpg", "./pics/background/2.jpg", "./pics/background/3.jpg", "./pics/background/2.jpg", "./pics/background/1.jpg"]

// $(function() {
  
//   setInterval(changeImage, 2000);

// });

// function changeImage(){
// 	$('.img').css("background-image", 'url(' +listaimg[index]+')');
//   $('.img:before').css("background-image", 'url(' +listaimg[index]+')');
// 	index++

// 	if(index == listaimg.length-1){
// 		index = 0;
//   }
// }