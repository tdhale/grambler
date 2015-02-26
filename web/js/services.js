'use strict';

angular.module('gramblerServices', [])

.factory('wordService', function($http) {
    var anagrams = [];
    return {
        getAnagrams: function(word) {
            return $http.get("http://grambler.elasticbeanstalk.com/grambler?w=" + escape(word), {cache:true})
            .then(function(resp) {
                    anagrams = resp.data.Anagrams;
                    return anagrams;
                }, function(err) {
                    // err.status will contain the status code
                    console.error('ERR', err);
                    anagrams = [{'word':'cannot reach grambler service'},{'word':'check your connection'}];
                    return anagrams;
                }
            );
        }
    }
});
