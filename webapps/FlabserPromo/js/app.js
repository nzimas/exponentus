/* Auto close navbar on click */

$('.navbar-brand, .navbar-nav > li > a').click(function() {
    if (!$(this).hasClass('dropdown-toggle')) {
        $('.navbar-collapse').collapse('hide');
    }
});

/* Change navbar class on scroll */

$('.wrapper').waypoint(function(wp) {
    $('.navbar').toggleClass('js-navbar-top');
    $('.navbar.js-toggle-class').toggleClass('navbar-default navbar-inverse');
    return false;
}, {
    offset: '-20px'
});

/* Change navbar class on collapse/uncollapse in its top position */

$('.navbar .navbar-collapse').on('show.bs.collapse', function() {
    $('.navbar.js-navbar-top').toggleClass('navbar-default navbar-inverse');
    $('.navbar').toggleClass('js-toggle-class');
});

$('.navbar .navbar-collapse').on('hide.bs.collapse', function() {
    $('.navbar.js-navbar-top').toggleClass('navbar-default navbar-inverse');
    $('.navbar').toggleClass('js-toggle-class');
});

/**
 * Smooth scroll to anchor
 */

$(function() {
    $('a[href*=#]:not([href=#])').click(
        function() {
            if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
                var target = $(this.hash);
                target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
                if (target.length) {
                    $('html,body').animate({
                        scrollTop: (target.offset().top - 49) // 50px offset for navbar menu
                    }, 500);
                    return false;
                }
            }
        });
});

/**
 * Contact form
 */

$(document).ready(function(e) {
    $('form[name=contact_us]').submit(function(e) {
        $form = $(this);
        $.ajax({
            url: '?id=sendmail',
            type: 'POST',
            data: $form.serialize(),
            dataType: 'json',
            beforeSend: function(xhr) {
                $form.fadeTo('slow', 0.33);
                $('button', $form).attr('disabled', true);
                $('.has-error', $form).removeClass('has-error');
                $('.help-block', $form).html('');
                $('#form_message').removeClass('alert-success').html('');
            },
            success: function(json, status) {
                if (json.error) {
                    // Error messages
                    if (json.error.name) {
                        $('input[name=name]', $form).parent().addClass('has-error');
                        $('input[name=name]', $form).next('.help-block').html(json.error.name);
                    }
                    if (json.error.email) {
                        $('input[name=email]', $form).parent().addClass('has-error');
                        $('input[name=email]', $form).next('.help-block').html(json.error.email);
                    }
                    if (json.error.message) {
                        $('textarea[name=message]', $form).parent().addClass('has-error');
                        $('textarea[name=message]', $form).next('.help-block').html(json.error.message);
                    }
                    if (json.error.recaptcha) {
                        $('#form-captcha .help-block').addClass('has-error');
                        $('#form-captcha .help-block').html(json.error.recaptcha);
                    }
                }
                // Refresh Captcha
                // grecaptcha.reset();
                //
                if (json.success) {
                    $('#form_message').addClass('alert-success').html(json.success);
                    setTimeout(function() {
                        $('#form_message').removeClass('alert-success').html('');
                    }, 4000);
                }

            },
            complete: function(xhr, status) {
                $form.fadeTo('fast', 1);
                $('button', $form).attr('disabled', false);
            }
        });

        return false;
    });
});
