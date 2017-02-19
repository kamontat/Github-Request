# Github-Request [![Build Status](https://travis-ci.org/kamontat/Github-Request.svg?branch=master)](https://travis-ci.org/kamontat/Github-Request)  [![Documentation Status](https://readthedocs.org/projects/github-request/badge/?version=latest)](http://github-request.readthedocs.io/en/latest/?badge=latest)  [![CircleCI](https://circleci.com/gh/kamontat/Github-Request.svg?style=shield)](https://circleci.com/gh/kamontat/Github-Request)  [![codecov](https://codecov.io/gh/kamontat/Github-Request/branch/master/graph/badge.svg)](https://codecov.io/gh/kamontat/Github-Request)  
request issue in github

# I WILL UPDATE ALL DOCUMENT *QUICK* AS I CAN #

If you fork repository, and want to update the original repository you can see more in this link
http://stackoverflow.com/questions/7244321/how-do-i-update-a-github-forked-repository

### Design
- [Designing Page](https://github.com/kamontat/Github-Request/tree/master/design/picture)

### NEW BUILD & RUN
- To build you just call `./gradew build`(MacOS, Linux) or `gradew build`(WinOS)
  - In build commend do a lot of thing;
    1. `clean` - remove old version, to avoid conflicts
    2. `gendocs` - create javadoc in path `docs`
    3. `genversionclass` - create Version.java inside path `com.kamontat.model.management` that contain version of current program
    4. other common commend in build function like `:compileJava`, `:processResources`, `:test` etc.
    5. `info` - to output all information inside this program
    
# TODO-LIST

## S-Major
- [ ] change code to [MVP](https://blacklenspub.com/mvp-คืออะไร-แล้วเกี่ยวกะไรกับ-android-7a0460d7cd49#.97fv43hct) Pattern

## Major
- [ ] **Login Page**
- [ ] **User Management Page**
    - [X] **Menu Bar**
      - [X] Multi-Selected in User Page
        - [X] use text file
        - [X] use org
      - [ ] Account Management
        - [ ] limit
        - [ ] myself information
        - [ ] logout
      - [X] Exit
- [ ] **Repository Management Page** - add repository to the app
- [ ] **Account Management Page** - manage user and repo in one place
- [ ] **Download Page** - download the selected user repo
- [ ] **Issue Management Page** - create and watch issue in user repo

## Minor (Optional)
- [ ] back button
