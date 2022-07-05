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
            cardType: "",
            colorCard: "GOLD",

            currentCardBackground: Math.floor(Math.random() * 25 + 1), // just for fun :D
            cardMonth: "",
            cardYear: "",
            cardCvv: "",
            minCardYear: new Date().getFullYear(),
            cardNumberTemp: "",
            isCardFlipped: false,
            focusElementStyle: null,
            isInputFocused: false
        }
    },
    created() {

        axios.get('/api/clients/current')
            .then(datos => {
                this.client = datos.data;
                console.log(this.client);

                this.cardsDebit = datos.data.cards.filter(card => card.type == "DEBIT" && card.enabled).sort((a, b) => { return a.id - b.id });
                console.log(this.cardsDebit);

                this.cardsCredit = datos.data.cards.filter(card => card.type == "CREDIT" && card.enabled).sort((a, b) => { return a.id - b.id });
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
        createAddCard() {
            axios.post('/api/clients/current/cards', `cardType=${this.cardType}&colorCard=${this.colorCard}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    console.log('Card Created!');
                    window.location.href = '/web/cards.html'
                })
                .catch(error => console.warn(error.message))
        },
        flipCard(status) {
            this.isCardFlipped = status;
        },
        focusInput(e) {
            this.isInputFocused = true;
            let targetRef = e.target.dataset.ref;
            let target = this.$refs[targetRef];
            this.focusElementStyle = {
                width: `${target.offsetWidth}px`,
                height: `${target.offsetHeight}px`,
                transform: `translateX(${target.offsetLeft}px) translateY(${target.offsetTop}px)`
            }
        },

    },
    computed: {

    },

}).mount('#app')

// nav
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

// end nav

// image uploader scripts 

var $dropzone = $('.image_picker'),
    $droptarget = $('.drop_target'),
    $dropinput = $('#inputFile'),
    $dropimg = $('.image_preview'),
    $remover = $('[data-action="remove_current_image"]');

$dropzone.on('dragover', function () {
    $droptarget.addClass('dropping');
    return false;
});

$dropzone.on('dragend dragleave', function () {
    $droptarget.removeClass('dropping');
    return false;
});

$dropzone.on('drop', function (e) {
    $droptarget.removeClass('dropping');
    $droptarget.addClass('dropped');
    $remover.removeClass('disabled');
    e.preventDefault();

    var file = e.originalEvent.dataTransfer.files[0],
        reader = new FileReader();

    reader.onload = function (event) {
        $dropimg.css('background-image', 'url(' + event.target.result + ')');
    };

    console.log(file);
    reader.readAsDataURL(file);

    return false;
});

$dropinput.change(function (e) {
    $droptarget.addClass('dropped');
    $remover.removeClass('disabled');
    $('.image_title input').val('');

    var file = $dropinput.get(0).files[0],
        reader = new FileReader();

    reader.onload = function (event) {
        $dropimg.css('background-image', 'url(' + event.target.result + ')');
    }

    reader.readAsDataURL(file);
});

$remover.on('click', function () {
    $dropimg.css('background-image', '');
    $droptarget.removeClass('dropped');
    $remover.addClass('disabled');
    $('.image_title input').val('');
});

$('.image_title input').blur(function () {
    if ($(this).val() != '') {
        $droptarget.removeClass('dropped');
    }
});

// image uploader scripts