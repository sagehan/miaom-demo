[boot]: http://boot-clj.com
[2]: http://localhost:8000
[Hoplon]: http://hoplon.io
[Datomic]: http://www.datomic.com/
[datascript]: https://github.com/tonsky/datascript
[6]: http://tonsky.me/blog/the-web-after-tomorrow/
[7]: http://localhost:5252
[LICENSE]: https://www.eclipse.org/legal/epl-v10.html

# miaomfood

A simple demo built with full clojure(script) technology stack which use
hoplon framework to achieve reactive!

Bonus, the design prototype is also included within this repo, you can find sketch
file under `Design` directory! Without my approval ,you have no rights to reuse
this design prototype in any business product, it just for learning use case!

I recommend you guys read this article: [The Web After Tomorrow][6].

This project mainly uses those technology:

1. [Hoplon] Reactive framework which compete with react/Om.
2. [Datomic] Distributed NoSQL database.
3. [datascript] Front-end NoSQL memory database, mock datomic!

## Dependencies

- java 1.8
- [datomic](https://my.datomic.com/downloads/free)  0.9.5359
- [boot]

## Usage

To follow this guide, you need to change to directory `Code/app_Web/miaomfood`!

### Startup Datomic transactor!

Before any playing around, please make sure that datomic transactor is running!
You can read [this](http://docs.datomic.com/getting-started.html) article to get start!

### Play Around

1. you can play with this demo quickly after start datomic transactor like
   this!

   ```bash
    $ boot play
    ```

### Front-end Development
1. Start the `cljs-dev` task. In a terminal run:
    ```bash
    $ boot cljs-dev
    ```
    This will give you a  Hoplon development setup with:
    - auto compilation on file changes
    - audible warning for compilation success or failures
    - auto reload the html(and other static files like .css, .hl) page on changes
    - Clojurescript REPL

2. Go to <http://localhost:8000> in your browser. You should see the SPA page.

3. If you edit and save a file, the task will recompile the code and reload the
   browser to show the updated version.

### Back-end Development
1. First
    ```bash
    $ boot cider repl
    ```
    , or just run:
    ```bash
    $ boot repl
    ```

2. In repl, start dev task:
    ```repl
    boot.user=> (dev)
    ```

3. Start the server:
    ```repl
    boot.user=> (start)
    ```

### Production
1. Run the `prod` task. In a terminal run:
    ```bash
    $ boot prod
    ```

2. The compiled files will be on the `target/` directory. This will use
   advanced compilation and prerender the html.

## License
Copyright © 2016 **Truewen**. All rights reserved.
Licensed under Eclipse Public License (see [LICENSE]).
