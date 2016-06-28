/**
 * Created by clove on 6/12/16.
 */

(function () {
    'use strict';

    angular
        .module('gpmrApp')
        .controller('SidebarController', SidebarController);

    SidebarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function SidebarController($state, Auth, Principal, ProfileService, LoginService) {
        var vm = this;
        vm.url = window.location.href.split('?')[0];

        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function (response) {
            vm.inProduction = response.inProduction;
            vm.swaggerDisabled = response.swaggerDisabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.$state = $state;

        function login() {
            LoginService.open();
        }

        function logout() {
            Auth.logout();
            $state.go('home');
        }

        $('#sidebar-menu').find('a[class=col-menu]').on('click', function (ev) {
            var $li = $(this).parent();

            if ($li.is('.active')) {
                $li.removeClass('active active-sm');
                $('ul:first', $li).slideUp(function () {
                    setContentHeight();
                });
            } else {
                // prevent closing menu if we are on child menu
                if (!$li.parent().is('.child_menu')) {
                    $('#sidebar-menu').find('li').removeClass('active active-sm');
                    $('#sidebar-menu').find('li ul').slideUp();
                }

                $li.addClass('active');

                $('ul:first', $li).slideDown(function () {
                    setContentHeight();
                });
            }

            // toggle small or large menu

        });

        $('#menu_toggle').on('click', function () {
            if ($('body').hasClass('nav-md')) {
                $('#sidebar-menu').find('li.active ul').hide();
                $('#sidebar-menu').find('li.active').addClass('active-sm').removeClass('active');
            } else {
                $('#sidebar-menu').find('li.active-sm ul').show();
                $('#sidebar-menu').find('li.active-sm').addClass('active').removeClass('active-sm');
            }

            $('body').toggleClass('nav-md nav-sm');

            setContentHeight();
        });

        // check active menu
        $('#sidebar-menu').find('a[href="' + vm.url + '"]').parent('li').addClass('current-page');

        $('#sidebar-menu').find('a').filter(function () {
            return this.href == vm.url;
        }).parent('li').addClass('current-page').parents('ul').slideDown(function () {
            setContentHeight();
        }).parent().addClass('active')

        var setContentHeight = function () {
            // reset height
            $('.right_col').css('min-height', $(window).height());

            var bodyHeight = $('body').outerHeight(),
                footerHeight = $('body').hasClass('footer_fixed') ? 0 : $('footer').height(),
                leftColHeight = $('.left_col').eq(1).height() + $('.sidebar-footer').height(),
                contentHeight = bodyHeight < leftColHeight ? leftColHeight : bodyHeight;

            // normalize content
            contentHeight -= $('.nav_menu').height() + footerHeight;

            $('.right_col').css('min-height', contentHeight);
        };

        // check active menu
        $('#sidebar-menu').find('a[href="' + vm.url + '"]').parent('li').addClass('current-page');

        $('#sidebar-menu').find('a').filter(function () {
            return this.href == vm.url;
        }).parent('li').addClass('current-page').parents('ul').slideDown(function () {
            setContentHeight();
        }).parent().addClass('active');

        $('.collapse-link').on('click', function () {
            var $BOX_PANEL = $(this).closest('.x_panel'),
                $ICON = $(this).find('i'),
                $BOX_CONTENT = $BOX_PANEL.find('.x_content');

            // fix for some div with hardcoded fix class
            if ($BOX_PANEL.attr('style')) {
                $BOX_CONTENT.slideToggle(200, function () {
                    $BOX_PANEL.removeAttr('style');
                });
            } else {
                $BOX_CONTENT.slideToggle(200);
                $BOX_PANEL.css('height', 'auto');
            }

            $ICON.toggleClass('fa-chevron-up fa-chevron-down');
        });

        $('.close-link').click(function () {
            var $BOX_PANEL = $(this).closest('.x_panel');

            $BOX_PANEL.remove();
        });

/*
        $('[data-toggle="tooltip"]').tooltip({
            container: 'body'
        });
        */

    }
})();
