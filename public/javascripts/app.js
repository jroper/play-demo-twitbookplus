'use strict';

requirejs.config({
    shim: {
        'webjars!bootstrap.js':{deps: ['webjars!jquery.js']}
    }
});

require(["webjars!knockout.js", 'webjars!jquery.js', "/routes.js", "webjars!bootstrap.js"], function(ko) {

    function TwitBookPlusViewModel() {
        var self = this;

        // Sign up stuff
        self.loggedInUser = ko.observable();
        self.signInForm = ko.observable();
        self.signUpForm = ko.observable();

        // User search
        self.userSearchField = ko.observable();
        self.userSearchResults = ko.observableArray();

        // Status update
        self.statusUpdateField = ko.observable();

        // Pages
        self.profile = ko.observable();
        self.following = ko.observable();
        self.followers = ko.observable();
        self.feed = ko.observable();
        self.trending = ko.observable();

        self.userController = routes.controllers.UserController;
        self.feedController = routes.controllers.FeedController;

        // Behaviours
        self.like = function(item) {
            ajax(self.feedController.like(item.id)).done(function() {
                item.numLikes(item.numLikes() + 1);
                item.canLike(false);
            });
        };

        self.unlike = function(item) {
            ajax(self.feedController.unlike(item.id)).done(function() {
                item.numLikes(item.numLikes() - 1);
                item.canLike(true);
            });
        };

        self.viewTrending = function() {
            goTo("trending");
            ajax(self.feedController.trending()).done(self.trending);
        };

        self.updateStatus = function() {
            self.statusUpdateField("");
            $("#statusUpdateDialog").modal("show");
        };

        self.submitStatusUpdate = function() {
            ajax(self.feedController.updateStatus(), {
                data: JSON.stringify({status: self.statusUpdateField()}),
                contentType: "application/json"
            }).done(function() {
                self.closeDialogs();
                if (self.feed() != null) {
                    self.viewFeed();
                }
            });
        };

        function processFeed(item) {
            item.numLikes = ko.observable(item.likes.length);
            item.canLike = ko.observable(item.likes.indexOf(self.loggedInUser().id) < 0);
        }

        self.viewFeed = function() {
            goTo("feed", "/");
            ajax(self.feedController.getFeed()).done(function(feed) {
                feed.forEach(processFeed);
                self.feed({feed: ko.observableArray(feed)});
            });
        };

        self.viewProfile = function(user) {
            goTo("profile", "/profile/" + user.id);
            var userOb = ko.observable();
            var feedOb = ko.observableArray();
            self.profile({user: userOb, feed: feedOb});
            ajax(self.userController.getUser(user.id)).done(userOb);
            ajax(self.feedController.getFeedForUser(user.id)).done(function(feed) {
                feed.forEach(processFeed);
                feedOb(feed);
            });
        };

        self.viewFeedForUser = function(user) {
            goTo("profile", "/profile/" + user.id + "/feed");
            ajax(self.feedController.getFeedForUser(user.id)).done(function(feed) {
                self.feed({feed: ko.observableArray(feed), user: user});
            });
        };

        self.viewFollowing = function(user) {
            if (user.id == undefined) {
                user = self.loggedInUser()
            }
            if (user.id == self.loggedInUser().id) {
                goTo("following");
            } else {
                goTo("following", "/profile/" + user.id + "/following")
            }
            ajax(self.userController.getUser(user.id)).done(function(fullUser) {
                fullUser.following.forEach(setupFollowProps);
                self.following({following: ko.observableArray(fullUser.following), user: fullUser});
            });
        };

        self.viewFollowers = function(user) {
            if (user.id == undefined) {
                user = self.loggedInUser()
            }
            if (user.id == self.loggedInUser().id) {
                goTo("followers");
            } else {
                goTo("followers", "/profile/" + user.id + "/followers")
            }
            ajax(self.userController.getUser(user.id)).done(function(fullUser) {
                fullUser.followers.forEach(setupFollowProps);
                self.followers({followers: ko.observableArray(fullUser.followers), user: fullUser});
            });
        };

        self.userSearch = function() {
            self.userSearchResults([]);
            self.userSearchField("");
            $("#userSearchDialog").modal("show");
        };

        self.followUser = function(user) {
            ajax(self.userController.followUser(user.id)).done(function() {
                // Update dialog
                user.canUnfollow(true);
                user.canFollow(false);
                self.loggedInUser().following.push(user);
                if (self.following() != null && self.following().user.id == self.loggedInUser().id) {
                    self.following().following.push(user);
                }
                if (self.followers() != null && self.followers().user.id == user.id) {
                    self.followers().followers.remove(function(u) {return user.id == u.id});
                }
            });
        };

        self.unfollowUser = function(user) {
            ajax(self.userController.unfollowUser(user.id)).done(function() {
                // Update dialog
                user.canFollow(true);
                user.canUnfollow(false);
                self.loggedInUser().following = self.loggedInUser().following.filter(function(u) {return user.id != u.id});
                if (self.following() != null && self.following().user.id == self.loggedInUser().id) {
                    self.following().following.remove(function(u) {return user.id == u.id});
                }
                if (self.followers() != null && self.followers().user.id == user.id) {
                    self.followers().followers.push(user);
                }
            });
        };

        // Search on key press
        self.userSearchField.subscribe(function(value) {
            if (value.length > 2) {
                ajax(self.userController.search(value)).done(function(results) {
                    results.forEach(setupFollowProps);
                    self.userSearchResults(results);
                })
            } else {
                self.userSearchResults([]);
            }
        });

        function setupFollowProps(user) {
            user.canFollow = ko.observable(false);
            user.canUnfollow = ko.observable(false);
            if (user.id == self.loggedInUser().id) {
                // Nothing
            } else if (self.loggedInUser().following.some(function(u) { return u.id == user.id; })) {
                user.canUnfollow(true);
            } else {
                user.canFollow(true);
            }
        };

        self.signIn = function() {
            self.signInForm({email: "", password: ""});
            $("#signInDialog").modal("show");
        };

        self.logIn = function() {
            ajax(self.userController.signIn(), {
                data: JSON.stringify(self.signInForm()),
                contentType: "application/json"
            }).done(function(data) {
                self.loggedInUser(data);
                self.navigateToUrl(window.location.pathname);
            });
        };

        self.logOut = function() {
            ajax(self.userController.logOut()).done(function() {
                self.loggedInUser(null);
                goTo("feed", "/")
            });
        };

        self.signUp = function() {
            $("#signInDialog").modal("hide");
            self.signUpForm({email: "", name: "", password: ""});
            $("#signUpDialog").modal("show");
        };

        self.closeDialogs = function() {
            $(".modal").modal("hide");
        };

        self.submitSignUp = function() {
            ajax(self.userController.newUser(), {
                data: JSON.stringify(self.signUpForm()),
                contentType: "application/json"
            }).done(function(data) {
                self.checkCurrentUser();
                self.closeDialogs();
            });
        };

        self.checkCurrentUser = function(cb) {
            if (self.loggedInUser() == null) {
                ajax(self.userController.current()).done(function(data, response) {
                    self.loggedInUser(data);
                    if (cb != undefined) {
                        cb();
                    }
                });
            } else if (cb != undefined) {
                cb();
            }
        };

        self.navigateToUrl = function(path) {
            model.checkCurrentUser(function() {

                function route(regex, cb) {
                    function r() {
                        this.regex = new RegExp(regex);
                        this.cb = cb;
                    }
                    return new r();
                }

                [
                    route("^/$", function() {self.viewFeed()}),
                    route("^/profile/([^/]*$)", function(m) { self.viewProfile({id: m[1]})}),
                    route("^/following$", function() {self.viewFollowing({})}),
                    route("^/profile/([^/]*)/following$", function(m) {self.viewFollowing({id: m[1]})}),
                    route("^/followers$", function() {self.viewFollowers({})}),
                    route("^/profile/([^/]*)/followers$", function(m) {self.viewFollowers({id: m[1]})}),
                    route("^/trending$", function() {self.viewTrending()})
                ].some(function(r) {
                    var m = r.regex.exec(path);
                    if (m) {
                        r.cb(m);
                        return true;
                    }
                    return false;
                });
            });
        };

        function ajax(route, params) {
            return $.ajax($.extend(params, route));
        }

        function resetView() {
            // In case there are any modals open
            self.closeDialogs();

            // Close all other pages
            self.profile(null);
            self.following(null);
            self.followers(null);
            self.feed(null);
            self.trending(null);
        }

        function goTo(name, path) {
            resetView();

            if (path == undefined) {
                path = "/" + name;
            }

            if (path != window.location.pathname) {
                window.history.pushState({name: name, path: path}, path, path);
            }
        }
    }

    var model = new TwitBookPlusViewModel();
    ko.applyBindings(model);

    // Decide where we are
    model.navigateToUrl(window.location.pathname);

    window.onpopstate = function(e) {
        model.navigateToUrl(e.state.path);
    };

})