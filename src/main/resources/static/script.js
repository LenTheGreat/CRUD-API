function fillPermanentAddress(){
        var checkBox = document.getElementById('checkBox');

        var homeAddress_street = document.getElementById('homeAddress_street');
        var homeAddress_city = document.getElementById('homeAddress_city');
        var homeAddress_province = document.getElementById('homeAddress_province');
        var homeAddress_zipCode = document.getElementById('homeAddress_zipCode');

        var permanentAddress_street = document.getElementById('permanentAddress_street');
        var permanentAddress_city = document.getElementById('permanentAddress_city');
        var permanentAddress_province = document.getElementById('permanentAddress_province');
        var permanentAddress_zipCode = document.getElementById('permanentAddress_zipCode');

    if (checkBox.checked)
    {
        console.log("checkbox clicked");
        permanentAddress_street.value =  homeAddress_street.value;
        permanentAddress_city.value = homeAddress_city.value;
        permanentAddress_province.value = homeAddress_province.value;
        permanentAddress_zipCode.value  = homeAddress_zipCode.value;

    } else
    {
        console.log("checkbox unclick");
        permanentAddress_street.value = "";
        permanentAddress_city.value = "";
        permanentAddress_province.value = "";
        permanentAddress_zipCode.value  = "";
    }
}

const myModal = document.getElementById('myModal')
const myInput = document.getElementById('myInput')

myModal.addEventListener('shown.bs.modal', () => {
  myInput.focus()
})