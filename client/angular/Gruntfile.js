module.exports = function (grunt) {
    'use strict';

    grunt.initConfig({
            pkg: grunt.file.readJSON('package.json'),
            jasmine: {
                unit: {
                    src: ['js/main/**/*.js'],
                    options: {
                        specs: 'js/test/**/*Spec.js',
                        vendor: [
                            'src/main/resources/static/bower/angular/*min.js',
                            'src/main/resources/static/bower/angular-mocks/angular-mocks.js',
                            'src/main/resources/static/bower/angular-resource/*min.js'
                        ],
                        keepRunner: true
                    }
                }
            },
            typescript: {
                base: {
                    src: ['src/main/**/*.ts', 'src/test/**/*.ts'],
                    dest: 'js',
                    options: {
                        target: 'es5',
                        sourceMap: true
                    }
                }
            }
    }
    )

    grunt.loadNpmTasks('grunt-typescript');
    grunt.loadNpmTasks('grunt-contrib-jasmine');

    grunt.registerTask('default', ['typescript:base', 'jasmine']);
}