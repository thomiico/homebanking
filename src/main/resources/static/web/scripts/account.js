Vue.createApp({
  data() {
    return {
      paramURL: "",
      dataB: [],
      accounts: [],
      client: [],
      bool: false,
    }
  },
  created() {
    const params = new Proxy(new URLSearchParams(window.location.search), {
      get: (searchParams, prop) => searchParams.get(prop),
    });
    // Get the value of "some_key" in eg "https://example.com/?some_key=some_value"
    this.paramURL = params.id; // "some_value"

    axios.get('http://localhost:8080/api/accounts/' + this.paramURL)
      .then(datos => {
        this.dataB = datos;
        console.log(this.dataB);

        this.accounts = datos.data.transactions;
        console.log(this.accounts);

        this.bool = true;
      })
      .catch(error => console.warn(error.message));

  },
  methods: {
    formatMoney(amount) {
      return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD', minimumFractionDigits: 2 }).format(amount);
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

// '.tbl-content' consumed little space for vertical scrollbar, scrollbar width depend on browser/os/platfrom. Here calculate the scollbar width .
$(window).on("load resize ", function() {
  var scrollWidth = $('.tbl-content').width() - $('.tbl-content table').width();
  $('.tbl-header').css({'padding-right':scrollWidth});
}).resize();