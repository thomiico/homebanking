Vue.createApp({
  data() {
    return {
      archivoJson: {},
      clients: [],
      client: {},
      firstName: "",
      lastName: "",
      email: "",
      firstNameEdit: "",
      lastNameEdit: "",
      emailEdit: "",
    }
  },
  created() {

    axios.get('http://localhost:8080/rest/clients')
      .then(datos => {
        this.archivoJson = datos;
        this.clients = datos.data._embedded.clients;

        console.log(this.clients)
      })
      .catch(error => console.warn(error.message))
  },
  methods: {
    addClient() {
      if (this.firstName != "" && this.lastName != "" && this.email != "") {


        axios.post('http://localhost:8080/rest/clients', {
          firstName: this.firstName,
          lastName: this.lastName,
          email: this.email
        })
          .then(function (response) {
            console.log(response);
          })
          .catch(function (error) {
            console.log(error);
          });
      }
    },
    deleteClient(client) {
      axios.delete(client._links.client.href)
        .then(location.reload())
    },

    editClient(param) {

      console.log(param);

      Swal.fire({

        title: `Modificar usuario: ${param.firstName} ${param.lastName}`,
        html:
          `<input class="swal2-input" value="${param.firstName}" id="nameEdit">` +
            `<input class="swal2-input" value="${param.lastName}" id="lastNameEdit">` +
            `<input class="swal2-input" value="${param.email}" id="emailEdit">`,

        showDenyButton: true,
        showCancelButton: true,
        confirmButtonText: 'Save',
        denyButtonText: `Don't save`,
      }).then((result) => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
          axios.put(param._links.client.href, {
            firstName: document.getElementById("nameEdit").value,
            lastName: document.getElementById("lastNameEdit").value,
            email: document.getElementById("emailEdit").value
          });
          Swal.fire('Saved!', '', 'success');
          location.reload();
        } else if (result.isDenied) {
          Swal.fire('Changes are not saved', '', 'info');
        }
      })
    
    }
  },
  computed: {

  }

}).mount('#app')