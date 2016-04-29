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
                            'bower_components/angular/*min.js',
                            'bower_components/angular-mocks/angular-mocks.js'
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
            },
            copy: {
                    main: {
                        files: [
                        {
                            cwd: 'js/main/ts',
                            expand: true,
                            src: ['**'],
                            dest: 'src/main/js/'
                        }]
                    }
            }
    }
    )

    grunt.loadNpmTasks('grunt-typescript');
    grunt.loadNpmTasks('grunt-contrib-jasmine');
    grunt.loadNpmTasks('grunt-contrib-copy');

    grunt.registerTask('default', ['typescript:base', 'jasmine','copy:main']);
}