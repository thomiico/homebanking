Vue.createApp({
  data() {
    return {
      client: [],
      accounts: [],
      transactions: [],
      totalTransactions: [],
      dolar: [],
      loans: [],
      loansAPI: [],
      cardsDebit: [],
      cardsCredit: [],

      loanType: "Select Your Type Loan:",
      loanPayments: 0,
      loanAmount: 0,
      loanAccount: "Select Your Account:",
      bool: false,

      submitted: false
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

        
      })
      .then(
        axios.get('/api/loans')
        .then(datos => {
          this.loansAPI = datos.data.filter(loanFilter => !this.client.loans.map(clientLoan => clientLoan.name).includes(loanFilter.name));
  
          console.log(this.loansAPI);
        })
        .catch(error => console.warn(error.message))
      )
      .catch(error => console.warn(error.message))

  },

  methods: {
    // submit form handler
    submit: function () {
      this.submitted = true;
    },
    // check or uncheck all
    checkAll: function (event) {
      this.selection.features = event.target.checked ? this.features : [];
    },
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
    createPreLoan() {
      if (this.loanPayments == 0 || this.loanAmount == 0 || this.loanAccount == 'Select Your Account:') {
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Something went wrong!',
          footer: '<a href="">Why do I have this issue?</a>'
        })
      } else {

        Swal.fire({
          title: 'Are you sure to request this loan?',
          text: "This loan contains interest!",
          icon: 'question',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: 'Yes, i want!'
        }).then((result) => {
          if (result.isConfirmed) {
            Swal.fire(
              'OK!',
              'We are verifying your data...',
              'info'
            )
            this.createConsultLoan();
          }
        })
      }
    },
    createConsultLoan() {
      let timerInterval
      Swal.fire({
        title: 'Checking data!',
        html: 'Corroborating data in <b></b> milliseconds.',
        timer: 4500,
        timerProgressBar: true,
        didOpen: () => {
          Swal.showLoading()
          const b = Swal.getHtmlContainer().querySelector('b')
          timerInterval = setInterval(() => {
            b.textContent = Swal.getTimerLeft()
          }, 100)
        },
        willClose: () => {
          clearInterval(timerInterval)
        }
      }).then((result) => {
        /* Read more about handling dismissals below */
        if (result.dismiss === Swal.DismissReason.timer) {
          console.log('I was closed by the timer')
          this.createLoan();
        }
      })
    },

    createLoan() {
      let object =
      {
        "loanID": this.loanType.id,
        "amount": this.loanAmount,
        "payments": this.loanPayments,
        "accountNumber": this.loanAccount.number
      }
      console.log(object);
      axios.post('/api/loans', object)

        .then(() => {
          console.log('created')
          Swal.fire(
            'Congratulations!',
            'You are apply for this loan',
            'success'
          )
          setTimeout(() => {
            window.location.href = "/web/accounts.html"
          }, 2000)
        })

        .catch(() => console.log('error'))
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
