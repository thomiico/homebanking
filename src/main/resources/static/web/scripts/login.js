Vue.createApp({
  data() {
    return {
      passwordType: "password",
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
        Swal.fire({
          position: 'top-end',
          icon: 'success',
          title: 'Successfully completed',
          toast: true,
          showConfirmButton: false,
          timer: 3500
        })

      })
      .catch(error => {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: error.response.data,
          toast: true,
          showConfirmButton: false,
          timer: 3500
        })

        console.log(error.response.data)

      })
  },
  methods: {
    toggleType() {
      this.passwordType = this.passwordType === "password" ? "text" : "password";
    },

    formatMoney(amount) {
      return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD', minimumFractionDigits: 2 }).format(amount);
    },

    redirect(id) {
      let url = "/web/account.html?id=" + id;
      window.location.href(url);
      return 1;
    },

    signIn() {
      if (this.email.length > 0 && this.password.length > 0) {

        axios.post('/api/login', `email=${this.email}&password=${this.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
          .then(response => {
            console.log('signed in!!!');
            window.location.href = '/web/accounts.html'
          })
          .catch(error => {
            Swal.fire({
              position: 'top-end',
              icon: 'error',
              title: "Invalid Password or Email",
              toast: true,
              showConfirmButton: false,
              timer: 1500
            })
          });
      }
      else {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: "Please, complete all the data",
          toast: true,
          showConfirmButton: false,
          timer: 1500
        })
      }
    },
    logOut() {
      axios.post('/api/logout')
        .then(response => {
          console.log('Log Out!!');
          window.location.href = '/web/index.html'
        })
    },
    hideOrShowPassword() {
      let password1, check;

      password1 = document.getElementById("password1");
      check = document.getElementById("show");

      if (check.checked) {
        password1.type = "text";
      }
      else {
        password1.type = "password";
      }
    },
    register() {
      axios.post('/api/clients', `firstName=${this.firstNameR}&lastName=${this.lastNameR}&email=${this.emailR}&password=${this.passwordR}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
        .then(response => {
          console.log('registered');
          this.password = this.passwordR;
          this.email = this.emailR;
          this.signIn();

        })
        .catch(error => {
          if (error.response.data == 'Missing data') {
            Swal.fire({
              position: 'top-end',
              icon: 'error',
              title: 'Missing Data',
              toast: true,
              showConfirmButton: false,
              timer: 1500
            })
        }
        if (error.response.data == 'Email already in use') {
          Swal.fire({
            position: 'top-end',
            icon: 'error',
            title: 'Email already in use',
            toast: true,
            showConfirmButton: false,
            timer: 1500
          })
        }
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
document.querySelector('.img__btn').addEventListener('click', function () {
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
