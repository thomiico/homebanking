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
      bool: false,
    }
  },
  created() {

    axios.get('/api/clients/current')
      .then(datos => {
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
    redirectCard() {
      window.location.href = '/web/create-cards.html'
    },
    deleteCard(id){
      axios.post(`/api/clients/current/cards/${id}`)
        .then(location.reload)
    },
    expireNow(card){
      let dayNow = new Date().getTime();

      console.log(card.thruDate)
      console.log(dayNow)
      
      let dateCard = new Date(card.thruDate).getTime();
      let difference = dateCard - dayNow;
      let toOverCome = 5270388157;

      if(difference < toOverCome){
        if(dateCard < dayNow){
          // card expirada 
          return true;
        }
        // else {
        //   // card a punto de expirar
        // }
      }
      return false;
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
