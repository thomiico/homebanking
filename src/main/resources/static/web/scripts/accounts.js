Vue.createApp({
  data() {
    return {
      client: [],
      accounts: [],
      transactions: [],
      totalTransactions: [],
      dolar: [],
      loans: [],
      cardsDebit: [],
      cardsCredit: [],
      accountType: "",
      bool: false,
    }
  },
  created() {

    axios.get('/api/clients/current') // axios.get('http://localhost:8080/api/clients/current',{headers:{'accept':'application/xml'}})
      .then(datos => {

        console.log(datos);

        this.client = datos.data;
        console.log(this.client);

        this.cardsDebit = datos.data.cards.filter(card => card.type == "DEBIT").sort((a, b) => { return a.id - b.id });
        console.log(this.cardsDebit);

        this.cardsCredit = datos.data.cards.filter(card => card.type == "CREDIT").sort((a, b) => { return a.id - b.id });
        console.log(this.cardsCredit);

        this.accounts = datos.data.accounts;
        console.log(this.accounts);

        this.loans = datos.data.loans;
        console.log(this.loans);

        this.accounts.forEach(acc => {
          this.transactions.push(acc.transactions)
        })
        console.log(this.transactions);

        this.transactions.forEach(acc => {
          acc.forEach(transact => {
            this.totalTransactions.push(transact);
          })
        })

        console.log(this.totalTransactions);

        this.bool = true;
        // let idLoader = document.getElementById("loader");
        // idLoader.classList.toggle("loader2")
      })
      .catch(error => console.warn(error.message))
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
    redirectBanner(){
      window.location.href = "https://www.binance.com/es"
    },
    separate(number) {
      const separate = 4;
      return number.split('').map((x, i) => (i > 0 && i % separate == 0) ? "-" + x : x).join('');
    },

    yearMonth(fecha) {
      let date = new Date(fecha);
      let year = date.getFullYear();
      let array_year = Array.from(year.toString()).slice(-2).join("");

      let month = date.getMonth() + 1;

      if (month < 10) {
        month = "0" + month;
      }

      let month_year = month + "/" + array_year;

      return month_year;

    },
    logOut() {
      axios.post('/api/logout')
        .then(response => {
          console.log('Log Out!!');
          window.location.href = '/web/index.html'
        })
    },
    addAccount() {
      axios.post('/api/clients/current/accounts', `type=${this.accountType}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
        .then(response => {
          console.log('Account Created!!');
          location.reload();
        })
    },
    deleteAccount(id){
      axios.post(`/api/clients/current/accounts/${id}`)
      .then(location.reload)
    }
  },
  computed: {

  }

}).mount('#app')

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


/* CAROUSEL */
$(document).ready(function () {
  $('.customer-logos').slick({
    slidesToShow: 6,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 1500,
    arrows: false,
    dots: false,
    pauseOnHover: false,
    responsive: [{
      breakpoint: 768,
      settings: {
        slidesToShow: 4
      }
    }, {
      breakpoint: 520,
      settings: {
        slidesToShow: 3
      }
    }]
  });
});