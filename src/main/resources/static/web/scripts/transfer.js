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
      transferType: "",
      accountSelectTransferOrigin: "",
      accountSelectTransferDestiny: "",
      accountNumberTransfer: 0,
      accountDescriptionTransfer: "",
      bool: false,
    }
  },
  created() {

    axios.get('http://localhost:8080/api/clients/current')
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
      let url = "http://localhost:8080/web/account.html?id=" + id;
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
    confirmTransfer() {

      Swal.fire({
        title: 'Are you sure to make the transfer?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes!'
      }).then((result) => {
        if (result.isConfirmed) {
          this.createTransfer()
          // Swal.fire(
          //   'Deleted!',
          //   'Your file has been deleted.',
          //   'success'
          // )
        }
      })
    },
    createTransfer() {
      if (this.accountSelectTransferDestiny != "" && this.accountSelectTransferOrigin != "" && this.accountNumberTransfer > 0 && this.accountDescriptionTransfer != "") {

        axios.post('http://localhost:8080/api/transactions', `description=${this.accountDescriptionTransfer}&amount=${this.accountNumberTransfer}&accountOriginNumber=${this.accountSelectTransferOrigin}&accountDestinyNumber=${this.accountSelectTransferDestiny}`)
          .then(response => {
            Swal.fire({
              icon: 'success', // success - error
              title: 'Transfer success',
              text: 'Thanks for use this Bank',
              footer: '<a href="./accounts.html">Back to Accounts</a>'
            })
            setInterval(function(){
              
                  window.location.href = './accounts.html'; 
          },3000);
            // window.location.href = "./accounts.html"
          })
          .catch(error => console.warn(error.message))
      }
      
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


//jQuery time
var current_fs, next_fs, previous_fs; //fieldsets
var left, opacity, scale; //fieldset properties which we will animate
var animating; //flag to prevent quick multi-click glitches

$(".next").click(function () {
  if (animating) return false;
  animating = true;

  current_fs = $(this).parent();
  next_fs = $(this).parent().next();

  //activate next step on progressbar using the index of next_fs
  $("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");

  //show the next fieldset
  next_fs.show();
  //hide the current fieldset with style
  current_fs.animate({ opacity: 0 }, {
    step: function (now, mx) {
      //as the opacity of current_fs reduces to 0 - stored in "now"
      //1. scale current_fs down to 80%
      scale = 1 - (1 - now) * 0.2;
      //2. bring next_fs from the right(50%)
      left = (now * 50) + "%";
      //3. increase opacity of next_fs to 1 as it moves in
      opacity = 1 - now;
      current_fs.css({
        'transform': 'scale(' + scale + ')',
        'position': 'absolute'
      });
      next_fs.css({ 'left': left, 'opacity': opacity });
    },
    duration: 800,
    complete: function () {
      current_fs.hide();
      animating = false;
    },
    //this comes from the custom easing plugin
    easing: 'easeInOutBack'
  });
});

$(".previous").click(function () {
  if (animating) return false;
  animating = true;

  current_fs = $(this).parent();
  previous_fs = $(this).parent().prev();

  //de-activate current step on progressbar
  $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");

  //show the previous fieldset
  previous_fs.show();
  //hide the current fieldset with style
  current_fs.animate({ opacity: 0 }, {
    step: function (now, mx) {
      //as the opacity of current_fs reduces to 0 - stored in "now"
      //1. scale previous_fs from 80% to 100%
      scale = 0.8 + (1 - now) * 0.2;
      //2. take current_fs to the right(50%) - from 0%
      left = ((1 - now) * 50) + "%";
      //3. increase opacity of previous_fs to 1 as it moves in
      opacity = 1 - now;
      current_fs.css({ 'left': left });
      previous_fs.css({ 'transform': 'scale(' + scale + ')', 'opacity': opacity });
    },
    duration: 800,
    complete: function () {
      current_fs.hide();
      animating = false;
    },
    //this comes from the custom easing plugin
    easing: 'easeInOutBack'
  });
});

$(".submit").click(function () {
  return false;
})
