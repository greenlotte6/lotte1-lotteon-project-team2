document.addEventListener('DOMContentLoaded', function(){
    
    const email2nd = document.getElementById('email-2nd');
    const emailSelect = document.getElementById('email-select');
    
    emailSelect.addEventListener('change', function() {
      if (this.value === 'custom') {
        email2nd.value = '';
      } else {
        email2nd.value = this.value;
      }
    });
    

    
});
