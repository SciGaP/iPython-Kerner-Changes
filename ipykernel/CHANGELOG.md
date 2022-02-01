# Changes in IPython kernel

<!-- <START NEW CHANGELOG ENTRY> -->

## 6.7.0

([Full Changelog](https://github.com/ipython/ipykernel/compare/v6.6.1...0be80cbc81927f4fb20343840bf5834b48884717))

### Enhancements made

- Add usage_request and usage_reply based on psutil [#805](https://github.com/ipython/ipykernel/pull/805) ([@echarles](https://github.com/echarles))

### Bugs fixed

- Removed DebugStdLib from arguments of attach [#839](https://github.com/ipython/ipykernel/pull/839) ([@JohanMabille](https://github.com/JohanMabille))
- Normalize debugger temp file paths on Windows [#838](https://github.com/ipython/ipykernel/pull/838) ([@kycutler](https://github.com/kycutler))
- Breakpoint in cell with leading empty lines may be ignored [#829](https://github.com/ipython/ipykernel/pull/829) ([@fcollonval](https://github.com/fcollonval))

### Maintenance and upkeep improvements

- Skip on PyPy, seem to fail. [#837](https://github.com/ipython/ipykernel/pull/837) ([@Carreau](https://github.com/Carreau))
- Remove pipx to fix conflicts [#835](https://github.com/ipython/ipykernel/pull/835) ([@Carreau](https://github.com/Carreau))
- Remove impossible skipif. [#834](https://github.com/ipython/ipykernel/pull/834) ([@Carreau](https://github.com/Carreau))

### Contributors to this release

([GitHub contributors page for this release](https://github.com/ipython/ipykernel/graphs/contributors?from=2022-01-03&to=2022-01-13&type=c))

[@Carreau](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3ACarreau+updated%3A2022-01-03..2022-01-13&type=Issues) | [@echarles](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Aecharles+updated%3A2022-01-03..2022-01-13&type=Issues) | [@fcollonval](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Afcollonval+updated%3A2022-01-03..2022-01-13&type=Issues) | [@JohanMabille](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3AJohanMabille+updated%3A2022-01-03..2022-01-13&type=Issues) | [@kycutler](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Akycutler+updated%3A2022-01-03..2022-01-13&type=Issues)

<!-- <END NEW CHANGELOG ENTRY> -->

## 6.6.1

([Full Changelog](https://github.com/ipython/ipykernel/compare/v6.6.0...bdce14b32ca8cc8f4b1635ea47200f0828ec1e05))

### Bugs fixed

- PR: do_one_iteration is a coroutine [#830](https://github.com/ipython/ipykernel/pull/830) ([@impact27](https://github.com/impact27))

### Maintenance and upkeep improvements

- Clean python 2 artifacts. Fix #826 [#827](https://github.com/ipython/ipykernel/pull/827) ([@penguinolog](https://github.com/penguinolog))

### Documentation improvements

- Fix title position in  changelog [#828](https://github.com/ipython/ipykernel/pull/828) ([@fcollonval](https://github.com/fcollonval))

### Contributors to this release

([GitHub contributors page for this release](https://github.com/ipython/ipykernel/graphs/contributors?from=2021-12-01&to=2022-01-03&type=c))

[@blink1073](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Ablink1073+updated%3A2021-12-01..2022-01-03&type=Issues) | [@ccordoba12](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Accordoba12+updated%3A2021-12-01..2022-01-03&type=Issues) | [@fcollonval](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Afcollonval+updated%3A2021-12-01..2022-01-03&type=Issues) | [@impact27](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Aimpact27+updated%3A2021-12-01..2022-01-03&type=Issues) | [@ivanov](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Aivanov+updated%3A2021-12-01..2022-01-03&type=Issues) | [@penguinolog](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Apenguinolog+updated%3A2021-12-01..2022-01-03&type=Issues)

## 6.6.0

([Full Changelog](https://github.com/ipython/ipykernel/compare/v6.5.1...9566304175d844c23a1f2b1d70c10df475ed2868))

### Enhancements made

- Set `debugOptions` for breakpoints in python standard library source [#812](https://github.com/ipython/ipykernel/pull/812) ([@echarles](https://github.com/echarles))
- Send `omit_sections` to IPython to choose which sections of documentation you do not want [#809](https://github.com/ipython/ipykernel/pull/809) ([@fasiha](https://github.com/fasiha))

### Bugs fixed

- Added missing `exceptionPaths` field to `debugInfo` reply [#814](https://github.com/ipython/ipykernel/pull/814) ([@JohanMabille](https://github.com/JohanMabille))

### Maintenance and upkeep improvements

- Test `jupyter_kernel_test` as downstream [#813](https://github.com/ipython/ipykernel/pull/813) ([@blink1073](https://github.com/blink1073))
- Remove `nose` dependency [#808](https://github.com/ipython/ipykernel/pull/808) ([@Kojoley](https://github.com/Kojoley))
- Add explicit encoding to open calls in debugger [#807](https://github.com/ipython/ipykernel/pull/807) ([@dlukes](https://github.com/dlukes))

### Contributors to this release

([GitHub contributors page for this release](https://github.com/ipython/ipykernel/graphs/contributors?from=2021-11-18&to=2021-12-01&type=c))

[@blink1073](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Ablink1073+updated%3A2021-11-18..2021-12-01&type=Issues) | [@dlukes](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Adlukes+updated%3A2021-11-18..2021-12-01&type=Issues) | [@echarles](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Aecharles+updated%3A2021-11-18..2021-12-01&type=Issues) | [@fasiha](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Afasiha+updated%3A2021-11-18..2021-12-01&type=Issues) | [@JohanMabille](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3AJohanMabille+updated%3A2021-11-18..2021-12-01&type=Issues) | [@Kojoley](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3AKojoley+updated%3A2021-11-18..2021-12-01&type=Issues)

## 6.5.1

([Full Changelog](https://github.com/ipython/ipykernel/compare/v6.5.0...1ef2017781435d54348fbb170b8c5d096e3e1351))

### Bugs fixed

- Fix the temp file name created by the debugger [#801](https://github.com/ipython/ipykernel/pull/801) ([@eastonsuo](https://github.com/eastonsuo))

### Maintenance and upkeep improvements

- Enforce labels on PRs [#803](https://github.com/ipython/ipykernel/pull/803) ([@blink1073](https://github.com/blink1073))
- Unpin `IPython`, and remove some dependencies on it. [#796](https://github.com/ipython/ipykernel/pull/796) ([@Carreau](https://github.com/Carreau))

### Contributors to this release

([GitHub contributors page for this release](https://github.com/ipython/ipykernel/graphs/contributors?from=2021-11-01&to=2021-11-18&type=c))

[@blink1073](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Ablink1073+updated%3A2021-11-01..2021-11-18&type=Issues) | [@Carreau](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3ACarreau+updated%3A2021-11-01..2021-11-18&type=Issues) | [@eastonsuo](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Aeastonsuo+updated%3A2021-11-01..2021-11-18&type=Issues)

## 6.5.0

([Full Changelog](https://github.com/ipython/ipykernel/compare/v6.4.2...e8d4f66e0f65e284aab444c53e9812dbbc814cb2))

### Bugs fixed

- Fix rich variables inspection [#793](https://github.com/ipython/ipykernel/pull/793) ([@fcollonval](https://github.com/fcollonval))
- Do not call `setQuitOnLastWindowClosed()` on a `QCoreApplication` [#791](https://github.com/ipython/ipykernel/pull/791) ([@stukowski](https://github.com/stukowski))

### Maintenance and upkeep improvements

- Drop `ipython_genutils` requirement [#792](https://github.com/ipython/ipykernel/pull/792) ([@penguinolog](https://github.com/penguinolog))

### Contributors to this release

([GitHub contributors page for this release](https://github.com/ipython/ipykernel/graphs/contributors?from=2021-10-20&to=2021-11-01&type=c))

[@ccordoba12](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Accordoba12+updated%3A2021-10-20..2021-11-01&type=Issues) | [@fcollonval](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Afcollonval+updated%3A2021-10-20..2021-11-01&type=Issues) | [@penguinolog](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Apenguinolog+updated%3A2021-10-20..2021-11-01&type=Issues) | [@stukowski](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Astukowski+updated%3A2021-10-20..2021-11-01&type=Issues)

## 6.4.2

([Full Changelog](https://github.com/ipython/ipykernel/compare/v6.4.1...231fd3c65f8a15e9e015546c0a6846e22df9ba2a))

### Enhancements made

- Enabled rich rendering of variables in the debugger [#787](https://github.com/ipython/ipykernel/pull/787) ([@JohanMabille](https://github.com/JohanMabille))

### Bugs fixed

- Remove setting of the eventloop function in the InProcessKernel [#781](https://github.com/ipython/ipykernel/pull/781) ([@rayosborn](https://github.com/rayosborn))

### Maintenance and upkeep improvements

- Add python version classifiers [#783](https://github.com/ipython/ipykernel/pull/783) ([@emuccino](https://github.com/emuccino))

### Contributors to this release

([GitHub contributors page for this release](https://github.com/ipython/ipykernel/graphs/contributors?from=2021-09-10&to=2021-10-19&type=c))

[@emuccino](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Aemuccino+updated%3A2021-09-10..2021-10-19&type=Issues) | [@JohanMabille](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3AJohanMabille+updated%3A2021-09-10..2021-10-19&type=Issues) | [@rayosborn](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Arayosborn+updated%3A2021-09-10..2021-10-19&type=Issues)

## 6.4.1

([Full Changelog](https://github.com/ipython/ipykernel/compare/v6.4.0...4da7623c1ae733f32c0792d70e7af283a7b19d22))

### Merged PRs

- debugpy is now a build requirement [#773](https://github.com/ipython/ipykernel/pull/773) ([@minrk](https://github.com/minrk))

### Contributors to this release

([GitHub contributors page for this release](https://github.com/ipython/ipykernel/graphs/contributors?from=2021-09-09&to=2021-09-10&type=c))

[@minrk](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Aminrk+updated%3A2021-09-09..2021-09-10&type=Issues)

## 6.4.0

([Full Changelog](https://github.com/ipython/ipykernel/compare/v6.3.1...1ba6b48a97877ff7a564af32c531618efb7d2a57))

### Enhancements made

- Make `json_clean` a no-op for `jupyter-client` >= 7 [#708](https://github.com/ipython/ipykernel/pull/708) ([@martinRenou](https://github.com/martinRenou))

### Bugs fixed

- Do not assume kernels have loops [#766](https://github.com/ipython/ipykernel/pull/766) ([@Carreau](https://github.com/Carreau))
- Fix undefined variable [#765](https://github.com/ipython/ipykernel/pull/765) ([@martinRenou](https://github.com/martinRenou))

### Maintenance and upkeep improvements

- Make `ipykernel` work without `debugpy` [#767](https://github.com/ipython/ipykernel/pull/767) ([@frenzymadness](https://github.com/frenzymadness))
- Stop using deprecated `recv_multipart` when using in-process socket. [#762](https://github.com/ipython/ipykernel/pull/762) ([@Carreau](https://github.com/Carreau))
- Update some warnings with instructions and version number. [#761](https://github.com/ipython/ipykernel/pull/761) ([@Carreau](https://github.com/Carreau))

### Contributors to this release

([GitHub contributors page for this release](https://github.com/ipython/ipykernel/graphs/contributors?from=2021-08-31&to=2021-09-09&type=c))

[@Carreau](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3ACarreau+updated%3A2021-08-31..2021-09-09&type=Issues) | [@frenzymadness](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Afrenzymadness+updated%3A2021-08-31..2021-09-09&type=Issues) | [@martinRenou](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3AmartinRenou+updated%3A2021-08-31..2021-09-09&type=Issues) | [@minrk](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Aminrk+updated%3A2021-08-31..2021-09-09&type=Issues)

## 6.3

## 6.3.1

([Full Changelog](https://github.com/ipython/ipykernel/compare/v6.3.0...0b4a8eaa080fc11e240ada9c44c95841463da58c))

### Merged PRs

- Add dependency on IPython genutils. [#756](https://github.com/ipython/ipykernel/pull/756) ([@Carreau](https://github.com/Carreau))

### Contributors to this release

([GitHub contributors page for this release](https://github.com/ipython/ipykernel/graphs/contributors?from=2021-08-30&to=2021-08-31&type=c))

[@Carreau](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3ACarreau+updated%3A2021-08-30..2021-08-31&type=Issues)

## 6.3.0

([Full Changelog](https://github.com/ipython/ipykernel/compare/6.2.0...07af2633ca88eda583e13649279a5b98473618a2))

### Enhancements made

- Add deep variable inspection [#753](https://github.com/ipython/ipykernel/pull/753) ([@JohanMabille](https://github.com/JohanMabille))
- Add `IPKernelApp.capture_fd_output` config to disable FD-level capture [#752](https://github.com/ipython/ipykernel/pull/752) ([@minrk](https://github.com/minrk))

### Maintenance and upkeep improvements

- Remove more `nose` test references [#750](https://github.com/ipython/ipykernel/pull/750) ([@blink1073](https://github.com/blink1073))
- Remove `nose` `skipIf` in favor of `pytest` [#748](https://github.com/ipython/ipykernel/pull/748) ([@Carreau](https://github.com/Carreau))
- Remove more `nose` [#747](https://github.com/ipython/ipykernel/pull/747) ([@Carreau](https://github.com/Carreau))
- Set up release helper plumbing [#745](https://github.com/ipython/ipykernel/pull/745) ([@afshin](https://github.com/afshin))
- Test downstream projects [#635](https://github.com/ipython/ipykernel/pull/635) ([@davidbrochart](https://github.com/davidbrochart))

### Contributors to this release

([GitHub contributors page for this release](https://github.com/ipython/ipykernel/graphs/contributors?from=2021-08-16&to=2021-08-30&type=c))

[@afshin](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Aafshin+updated%3A2021-08-16..2021-08-30&type=Issues) | [@blink1073](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Ablink1073+updated%3A2021-08-16..2021-08-30&type=Issues) | [@Carreau](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3ACarreau+updated%3A2021-08-16..2021-08-30&type=Issues) | [@ccordoba12](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Accordoba12+updated%3A2021-08-16..2021-08-30&type=Issues) | [@davidbrochart](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Adavidbrochart+updated%3A2021-08-16..2021-08-30&type=Issues) | [@JohanMabille](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3AJohanMabille+updated%3A2021-08-16..2021-08-30&type=Issues) | [@kevin-bates](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Akevin-bates+updated%3A2021-08-16..2021-08-30&type=Issues) | [@minrk](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Aminrk+updated%3A2021-08-16..2021-08-30&type=Issues) | [@SylvainCorlay](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3ASylvainCorlay+updated%3A2021-08-16..2021-08-30&type=Issues)

## 6.2

## 6.2.0

### Enhancements made

- Add Support for Message Based Interrupt [#741](https://github.com/ipython/ipykernel/pull/741) ([@afshin](https://github.com/afshin))

### Maintenance and upkeep improvements

- Remove some more dependency on nose/iptest [#743](https://github.com/ipython/ipykernel/pull/743) ([@Carreau](https://github.com/Carreau))
- Remove block param from get_msg() [#736](https://github.com/ipython/ipykernel/pull/736) ([@davidbrochart](https://github.com/davidbrochart))

## 6.1

## 6.1.0

### Enhancements made

- Implemented `richInspectVariable` request handler [#734](https://github.com/ipython/ipykernel/pull/734) ([@JohanMabille](https://github.com/JohanMabille))

### Maintenance and upkeep improvements

- Bump `importlib-metadata` limit for `python<3.8` [#738](https://github.com/ipython/ipykernel/pull/738) ([@ltalirz](https://github.com/ltalirz))

### Bug Fixes

- Fix exception raised by `OutStream.write` [#726](https://github.com/ipython/ipykernel/pull/726) ([@SimonKrughoff](https://github.com/SimonKrughoff))

## 6.0

## 6.0.3

- `KernelApp`: rename ports variable to avoid override [#731](https://github.com/ipython/ipykernel/pull/731) ([@amorenoz](https://github.com/amorenoz))

## 6.0.2

### Bugs fixed

- Add watchfd keyword to InProcessKernel OutStream initialization [#727](https://github.com/ipython/ipykernel/pull/727) ([@rayosborn](https://github.com/rayosborn))
- Fix typo in eventloops.py [#711](https://github.com/ipython/ipykernel/pull/711) ([@selasley](https://github.com/selasley))
- [bugfix] fix in setup.py (comma before appnope) [#709](https://github.com/ipython/ipykernel/pull/709) ([@jstriebel](https://github.com/jstriebel))

### Maintenance and upkeep improvements

- Add upper bound to dependency versions. [#714](https://github.com/ipython/ipykernel/pull/714) ([@martinRenou](https://github.com/martinRenou))
- Replace non-existing function. [#723](https://github.com/ipython/ipykernel/pull/723) ([@Carreau](https://github.com/Carreau))
- Remove unused variables [#722](https://github.com/ipython/ipykernel/pull/722) ([@Carreau](https://github.com/Carreau))
- Do not use bare except [#721](https://github.com/ipython/ipykernel/pull/721) ([@Carreau](https://github.com/Carreau))
- misc whitespace and line too long [#720](https://github.com/ipython/ipykernel/pull/720) ([@Carreau](https://github.com/Carreau))
- Formatting: remove semicolon [#719](https://github.com/ipython/ipykernel/pull/719) ([@Carreau](https://github.com/Carreau))
- Clean most flake8 unused import warnings. [#718](https://github.com/ipython/ipykernel/pull/718) ([@Carreau](https://github.com/Carreau))
- Minimal flake8 config [#717](https://github.com/ipython/ipykernel/pull/717) ([@Carreau](https://github.com/Carreau))
- Remove CachingCompiler's filename_mapper [#710](https://github.com/ipython/ipykernel/pull/710) ([@martinRenou](https://github.com/martinRenou))

## 6.0.1

- Fix Tk and asyncio event loops [#704](https://github.com/ipython/ipykernel/pull/704) ([@ccordoba12](https://github.com/ccordoba12))
- Stringify variables that are not json serializable in inspectVariable [#702](https://github.com/ipython/ipykernel/pull/702) ([@JohanMabille](https://github.com/JohanMabille))

## 6.0.0

([Full Changelog](https://github.com/ipython/ipykernel/compare/aba2179420a3fa81ee6b8a13f928bf9e5ce50716...6d04ad2bdccd0dc0daf20f8d53555174b5fefc7b))

IPykernel 6.0 is the first major release in about two years, that brings a number of improvements, code cleanup, and new
features to IPython.

You should be able to view all closed issues and merged Pull Request for this
milestone [on
GitHub](https://github.com/ipython/ipykernel/issues?q=milestone%3A6.0+is%3Aclosed+),
as for any major releases, we advise greater care when updating that for minor
release and welcome any feedback (~50 Pull-requests).

IPykernel 6 should contain all changes of the 5.x series, in addition to the
following non-exhaustive changes.

 - Support for the debugger protocol, when using `JupyterLab`, `RetroLab` or any
   frontend supporting the debugger protocol you should have access to the
   debugger functionalities.

 - The control channel on IPykernel 6.0 is run in a separate thread, this may
   change the order in which messages are processed, though this change was necessary
   to accommodate the debugger.

 - We now have a new dependency: `matplotlib-inline`, this helps to separate the
   circular dependency between IPython/IPykernel and  matplotlib.

 - All outputs to stdout/stderr should now be captured, including subprocesses
   and output of compiled libraries (blas, lapack....). In notebook
   server, some outputs that would previously go to the notebooks logs will now
   both head to notebook logs and in notebooks outputs. In terminal frontend
   like Jupyter Console, Emacs or other, this may ends up as duplicated outputs.

 - coroutines are now native (async-def) , instead of using tornado's
   `@gen.coroutine`

 - OutStreams can now be configured to report `istty() == True`, while this
   should make some output nicer (for example colored), it is likely to break
   others. Use with care.

### New features added

- Implementation of the debugger [#597](https://github.com/ipython/ipykernel/pull/597) ([@JohanMabille](https://github.com/JohanMabille))

### Enhancements made

- Make the `isatty` method of `OutStream` return `true` [#683](https://github.com/ipython/ipykernel/pull/683) ([@peendebak](https://github.com/peendebak))
- Allow setting cell name [#652](https://github.com/ipython/ipykernel/pull/652) ([@davidbrochart](https://github.com/davidbrochart))
- Try to capture all file descriptor output and err [#630](https://github.com/ipython/ipykernel/pull/630) ([@Carreau](https://github.com/Carreau))
- Implemented `inspectVariables` request [#624](https://github.com/ipython/ipykernel/pull/624) ([@JohanMabille](https://github.com/JohanMabille))
- Specify `ipykernel` in kernelspec [#616](https://github.com/ipython/ipykernel/pull/616) ([@SylvainCorlay](https://github.com/SylvainCorlay))
- Use `matplotlib-inline` [#591](https://github.com/ipython/ipykernel/pull/591) ([@martinRenou](https://github.com/martinRenou))
- Run control channel in separate thread [#585](https://github.com/ipython/ipykernel/pull/585) ([@SylvainCorlay](https://github.com/SylvainCorlay))

### Bugs fixed

- Remove references to deprecated `ipyparallel` [#695](https://github.com/ipython/ipykernel/pull/695) ([@minrk](https://github.com/minrk))
- Return len of item written to `OutStream` [#685](https://github.com/ipython/ipykernel/pull/685) ([@Carreau](https://github.com/Carreau))
- Call metadata methods on abort replies [#684](https://github.com/ipython/ipykernel/pull/684) ([@minrk](https://github.com/minrk))
- Fix keyboard interrupt issue in `dispatch_shell` [#673](https://github.com/ipython/ipykernel/pull/673) ([@marcoamonteiro](https://github.com/marcoamonteiro))
- Update `Trio` mode for compatibility with `Trio >= 0.18.0` [#627](https://github.com/ipython/ipykernel/pull/627) ([@mehaase](https://github.com/mehaase))
- Follow up `DeprecationWarning` Fix [#617](https://github.com/ipython/ipykernel/pull/617) ([@afshin](https://github.com/afshin))
- Flush control stream upon shutdown [#611](https://github.com/ipython/ipykernel/pull/611) ([@SylvainCorlay](https://github.com/SylvainCorlay))
- Fix Handling of `shell.should_run_async` [#605](https://github.com/ipython/ipykernel/pull/605) ([@afshin](https://github.com/afshin))
- Deacrease lag time for eventloop [#573](https://github.com/ipython/ipykernel/pull/573) ([@impact27](https://github.com/impact27))
- Fix "Socket operation on nonsocket" in downstream `nbclient` test. [#641](https://github.com/ipython/ipykernel/pull/641) ([@SylvainCorlay](https://github.com/SylvainCorlay))
- Stop control thread before closing sockets on it [#659](https://github.com/ipython/ipykernel/pull/659) ([@minrk](https://github.com/minrk))
- Fix debugging with native coroutines [#651](https://github.com/ipython/ipykernel/pull/651) ([@SylvainCorlay](https://github.com/SylvainCorlay))
- Fixup master build [#649](https://github.com/ipython/ipykernel/pull/649) ([@SylvainCorlay](https://github.com/SylvainCorlay))
- Fix parent header retrieval [#639](https://github.com/ipython/ipykernel/pull/639) ([@davidbrochart](https://github.com/davidbrochart))
- Add missing self [#636](https://github.com/ipython/ipykernel/pull/636) ([@Carreau](https://github.com/Carreau))
- Backwards compat with older versions of zmq [#665](https://github.com/ipython/ipykernel/pull/665) ([@mlucool](https://github.com/mlucool))

### Maintenance and upkeep improvements

- Remove pin on Jedi because that was already fixed in IPython [#692](https://github.com/ipython/ipykernel/pull/692) ([@ccordoba12](https://github.com/ccordoba12))
- Remove deprecated source parameter since 4.0.1 (2015) [#690](https://github.com/ipython/ipykernel/pull/690) ([@Carreau](https://github.com/Carreau))
- Remove deprecated `SocketABC` since 4.5.0 [#689](https://github.com/ipython/ipykernel/pull/689) ([@Carreau](https://github.com/Carreau))
- Remove deprecated profile options of `connect.py` [#688](https://github.com/ipython/ipykernel/pull/688) ([@Carreau](https://github.com/Carreau))
- Remove `ipykernel.codeutil` deprecated since IPykernel 4.3.1 (Feb 2016) [#687](https://github.com/ipython/ipykernel/pull/687) ([@Carreau](https://github.com/Carreau))
- Keep preferring `SelectorEventLoop` on Windows [#669](https://github.com/ipython/ipykernel/pull/669) ([@minrk](https://github.com/minrk))
- Add `Kernel.get_parent` to match `set_parent` [#661](https://github.com/ipython/ipykernel/pull/661) ([@minrk](https://github.com/minrk))
- Flush control queue prior to handling shell messages [#658](https://github.com/ipython/ipykernel/pull/658) ([@minrk](https://github.com/minrk))
- Add `Kernel.get_parent_header` [#657](https://github.com/ipython/ipykernel/pull/657) ([@minrk](https://github.com/minrk))
- Build docs only on Ubuntu: add jobs to check docstring formatting. [#644](https://github.com/ipython/ipykernel/pull/644) ([@Carreau](https://github.com/Carreau))
- Make deprecated `shell_streams` writable [#638](https://github.com/ipython/ipykernel/pull/638) ([@minrk](https://github.com/minrk))
- Use channel `get_msg` helper method [#634](https://github.com/ipython/ipykernel/pull/634) ([@davidbrochart](https://github.com/davidbrochart))
- Use native coroutines instead of tornado coroutines [#632](https://github.com/ipython/ipykernel/pull/632) ([@SylvainCorlay](https://github.com/SylvainCorlay))
- Make less use of `ipython_genutils` [#631](https://github.com/ipython/ipykernel/pull/631) ([@SylvainCorlay](https://github.com/SylvainCorlay))
- Run GitHub Actions on all branches [#625](https://github.com/ipython/ipykernel/pull/625) ([@afshin](https://github.com/afshin))
- Move Python-specific bits to ipkernel [#610](https://github.com/ipython/ipykernel/pull/610) ([@SylvainCorlay](https://github.com/SylvainCorlay))
- Update Python Requirement to 3.7 [#608](https://github.com/ipython/ipykernel/pull/608) ([@afshin](https://github.com/afshin))
- Replace import item from `ipython_genutils` to traitlets. [#601](https://github.com/ipython/ipykernel/pull/601) ([@Carreau](https://github.com/Carreau))
- Some removal of `ipython_genutils.py3compat`. [#600](https://github.com/ipython/ipykernel/pull/600) ([@Carreau](https://github.com/Carreau))
- Fixup `get_parent_header` call [#662](https://github.com/ipython/ipykernel/pull/662) ([@SylvainCorlay](https://github.com/SylvainCorlay))
- Update of `ZMQInteractiveshell`. [#643](https://github.com/ipython/ipykernel/pull/643) ([@Carreau](https://github.com/Carreau))
- Removed filtering of stack frames for testing [#633](https://github.com/ipython/ipykernel/pull/633) ([@JohanMabille](https://github.com/JohanMabille))
- Added 'type' field to variables returned by `inspectVariables` request [#628](https://github.com/ipython/ipykernel/pull/628) ([@JohanMabille](https://github.com/JohanMabille))
- Changed default timeout to 0.0 seconds for `stop_on_error_timeout` [#618](https://github.com/ipython/ipykernel/pull/618) ([@MSeal](https://github.com/MSeal))
- Attempt longer timeout [#615](https://github.com/ipython/ipykernel/pull/615) ([@SylvainCorlay](https://github.com/SylvainCorlay))
- Clean up release process and add tests [#596](https://github.com/ipython/ipykernel/pull/596) ([@afshin](https://github.com/afshin))
- Kernelspec: ensure path is writable before writing `kernel.json`. [#593](https://github.com/ipython/ipykernel/pull/593) ([@jellelicht](https://github.com/jellelicht))
- Add `configure_inline_support` and call it in the shell [#590](https://github.com/ipython/ipykernel/pull/590) ([@martinRenou](https://github.com/martinRenou))

### Documentation improvements

- Misc Updates to changelog for 6.0 [#686](https://github.com/ipython/ipykernel/pull/686) ([@Carreau](https://github.com/Carreau))
- Add 5.5.x Changelog entries [#672](https://github.com/ipython/ipykernel/pull/672) ([@blink1073](https://github.com/blink1073))
- Build docs only on ubuntu: add jobs to check docstring formatting. [#644](https://github.com/ipython/ipykernel/pull/644) ([@Carreau](https://github.com/Carreau))
- DOC: Autoreformat all docstrings. [#642](https://github.com/ipython/ipykernel/pull/642) ([@Carreau](https://github.com/Carreau))
- Bump Python to 3.8 in `readthedocs.yml` [#612](https://github.com/ipython/ipykernel/pull/612) ([@minrk](https://github.com/minrk))
- Fix typo [#663](https://github.com/ipython/ipykernel/pull/663) ([@SylvainCorlay](https://github.com/SylvainCorlay))
- Add release note to 5.5.0 about `stop_on_error_timeout` [#613](https://github.com/ipython/ipykernel/pull/613) ([@glentakahashi](https://github.com/glentakahashi))
- Move changelog to standard location [#604](https://github.com/ipython/ipykernel/pull/604) ([@afshin](https://github.com/afshin))
- Add changelog for 5.5 [#594](https://github.com/ipython/ipykernel/pull/594) ([@blink1073](https://github.com/blink1073))
- Change to markdown for changelog [#595](https://github.com/ipython/ipykernel/pull/595) ([@afshin](https://github.com/afshin))

### Deprecations in 6.0

- `Kernel`s now support only a single shell stream, multiple streams will now be ignored. The attribute
   `Kernel.shell_streams` (plural) is deprecated in ipykernel 6.0. Use `Kernel.shell_stream` (singular)
- `Kernel._parent_header` is deprecated, even though it was private. Use `.get_parent()` now.

### Removal in 6.0

- `ipykernel.codeutils` was deprecated since 4.x series (2016) and has been removed, please import similar
   functionalities from `ipyparallel`
- remove `find_connection_file` and `profile` argument of `connect_qtconsole` and `get_connection_info`, deprecated since IPykernel 4.2.2 (2016).

### Contributors to this release

([GitHub contributors page for this release](https://github.com/ipython/ipykernel/graphs/contributors?from=2021-01-11&to=2021-06-29&type=c))

[@afshin](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Aafshin+updated%3A2021-01-11..2021-06-29&type=Issues) | [@blink1073](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Ablink1073+updated%3A2021-01-11..2021-06-29&type=Issues) | [@Carreau](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3ACarreau+updated%3A2021-01-11..2021-06-29&type=Issues) | [@ccordoba12](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Accordoba12+updated%3A2021-01-11..2021-06-29&type=Issues) | [@davidbrochart](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Adavidbrochart+updated%3A2021-01-11..2021-06-29&type=Issues) | [@dsblank](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Adsblank+updated%3A2021-01-11..2021-06-29&type=Issues) | [@glentakahashi](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Aglentakahashi+updated%3A2021-01-11..2021-06-29&type=Issues) | [@impact27](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Aimpact27+updated%3A2021-01-11..2021-06-29&type=Issues) | [@ivanov](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Aivanov+updated%3A2021-01-11..2021-06-29&type=Issues) | [@jellelicht](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Ajellelicht+updated%3A2021-01-11..2021-06-29&type=Issues) | [@jkablan](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Ajkablan+updated%3A2021-01-11..2021-06-29&type=Issues) | [@JohanMabille](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3AJohanMabille+updated%3A2021-01-11..2021-06-29&type=Issues) | [@kevin-bates](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Akevin-bates+updated%3A2021-01-11..2021-06-29&type=Issues) | [@marcoamonteiro](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Amarcoamonteiro+updated%3A2021-01-11..2021-06-29&type=Issues) | [@martinRenou](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3AmartinRenou+updated%3A2021-01-11..2021-06-29&type=Issues) | [@mehaase](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Amehaase+updated%3A2021-01-11..2021-06-29&type=Issues) | [@minrk](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Aminrk+updated%3A2021-01-11..2021-06-29&type=Issues) | [@mlucool](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Amlucool+updated%3A2021-01-11..2021-06-29&type=Issues) | [@MSeal](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3AMSeal+updated%3A2021-01-11..2021-06-29&type=Issues) | [@peendebak](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Apeendebak+updated%3A2021-01-11..2021-06-29&type=Issues) | [@SylvainCorlay](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3ASylvainCorlay+updated%3A2021-01-11..2021-06-29&type=Issues) | [@tacaswell](https://github.com/search?q=repo%3Aipython%2Fipykernel+involves%3Atacaswell+updated%3A2021-01-11..2021-06-29&type=Issues)

## 5.5

### 5.5.5
* Keep preferring SelectorEventLoop on Windows. [#669](https://github.com/ipython/ipykernel/pull/669)

### 5.5.4
* Import ``configure_inline_support`` from ``matplotlib_inline`` if available [#654](https://github.com/ipython/ipykernel/pull/654)
   
### 5.5.3
* Revert Backport of #605: Fix Handling of ``shell.should_run_async`` [#622](https://github.com/ipython/ipykernel/pull/622)

### 5.5.2
**Note:** This release was deleted from PyPI since it had breaking changes.

* Changed default timeout to 0.0 seconds for stop_on_error_timeout. [#618](https://github.com/ipython/ipykernel/pull/618)

### 5.5.1
**Note:** This release was deleted from PyPI since it had breaking changes.

* Fix Handling of ``shell.should_run_async``. [#605](https://github.com/ipython/ipykernel/pull/605)

### 5.5.0
* kernelspec: ensure path is writable before writing `kernel.json`. [#593](https://github.com/ipython/ipykernel/pull/593)
* Add `configure_inline_support` and call it in the shell. [#590](https://github.com/ipython/ipykernel/pull/590)
* Fix `stop_on_error_timeout` to now properly abort `execute_request`'s that fall within the timeout after an error. [#572](https://github.com/ipython/ipykernel/pull/572)

## 5.4

### 5.4.3

* Rework `wait_for_ready` logic. [#578](https://github.com/ipython/ipykernel/pull/578)

### 5.4.2

* Revert \"Fix stop_on_error_timeout blocking other messages in
    queue\". [#570](https://github.com/ipython/ipykernel/pull/570)

### 5.4.1

* Invalid syntax in `ipykernel/log.py`. [#567](https://github.com/ipython/ipykernel/pull/567)

### 5.4.0

5.4.0 is generally focused on code quality improvements and tornado
asyncio compatibility.

* Add github actions, bail on asyncio patch for tornado 6.1.
    [#564](https://github.com/ipython/ipykernel/pull/564)
* Start testing on Python 3.9. [#551](https://github.com/ipython/ipykernel/pull/551)
* Fix stack levels for ipykernel\'s deprecation warnings and stop
    using some deprecated APIs. [#547](https://github.com/ipython/ipykernel/pull/547)
* Add env parameter to kernel installation [#541](https://github.com/ipython/ipykernel/pull/541)
* Fix stop_on_error_timeout blocking other messages in queue.
    [#539](https://github.com/ipython/ipykernel/pull/539)
* Remove most of the python 2 compat code. [#537](https://github.com/ipython/ipykernel/pull/537)
* Remove u-prefix from strings. [#538](https://github.com/ipython/ipykernel/pull/538)

## 5.3

### 5.3.4

* Only run Qt eventloop in the shell stream. [#531](https://github.com/ipython/ipykernel/pull/531)

### 5.3.3

* Fix QSocketNotifier in the Qt event loop not being disabled for the
    control channel. [#525](https://github.com/ipython/ipykernel/pull/525)

### 5.3.2

* Restore timer based event loop as a Windows-compatible fallback.
    [#523](https://github.com/ipython/ipykernel/pull/523)

### 5.3.1

* Fix \#520: run post_execute and post_run_cell on async cells
    [#521](https://github.com/ipython/ipykernel/pull/521)
* Fix exception causes in zmqshell.py [#516](https://github.com/ipython/ipykernel/pull/516)
* Make pdb on Windows interruptible [#490](https://github.com/ipython/ipykernel/pull/490)

### 5.3.0

5.3.0 Adds support for Trio event loops and has some bug fixes.

* Fix ipython display imports [#509](https://github.com/ipython/ipykernel/pull/509)
* Skip test_unc_paths if OS is not Windows [#507](https://github.com/ipython/ipykernel/pull/507)
* Allow interrupting input() on Windows, as part of effort to make pdb
    interruptible [#498](https://github.com/ipython/ipykernel/pull/498)
* Add Trio Loop [#479](https://github.com/ipython/ipykernel/pull/479)
* Flush from process even without newline [#478](https://github.com/ipython/ipykernel/pull/478)

## 5.2

### 5.2.1

* Handle system commands that use UNC paths on Windows
    [#500](https://github.com/ipython/ipykernel/pull/500)
* Add offset argument to seek in io test [#496](https://github.com/ipython/ipykernel/pull/496)

### 5.2.0

5.2.0 Includes several bugfixes and internal logic improvements.

* Produce better traceback when kernel is interrupted
    [#491](https://github.com/ipython/ipykernel/pull/491)
* Add `InProcessKernelClient.control_channel` for compatibility with
    jupyter-client v6.0.0 [#489](https://github.com/ipython/ipykernel/pull/489)
* Drop support for Python 3.4 [#483](https://github.com/ipython/ipykernel/pull/483)
* Work around issue related to Tornado with python3.8 on Windows
    ([#480](https://github.com/ipython/ipykernel/pull/480), [#481](https://github.com/ipython/ipykernel/pull/481))
* Prevent entering event loop if it is None [#464](https://github.com/ipython/ipykernel/pull/464)
* Use `shell.input_transformer_manager` when available
    [#411](https://github.com/ipython/ipykernel/pull/411)

## 5.1

### 5.1.4

5.1.4 Includes a few bugfixes, especially for compatibility with Python
3.8 on Windows.

* Fix pickle issues when using inline matplotlib backend
    [#476](https://github.com/ipython/ipykernel/pull/476)
* Fix an error during kernel shutdown [#463](https://github.com/ipython/ipykernel/pull/463)
* Fix compatibility issues with Python 3.8 ([#456](https://github.com/ipython/ipykernel/pull/456), [#461](https://github.com/ipython/ipykernel/pull/461))
* Remove some dead code ([#474](https://github.com/ipython/ipykernel/pull/474),
    [#467](https://github.com/ipython/ipykernel/pull/467))

### 5.1.3

5.1.3 Includes several bugfixes and internal logic improvements.

* Fix comm shutdown behavior by adding a `deleting` option to `close`
    which can be set to prevent registering new comm channels during
    shutdown ([#433](https://github.com/ipython/ipykernel/pull/433), [#435](https://github.com/ipython/ipykernel/pull/435))
* Fix `Heartbeat._bind_socket` to return on the first bind ([#431](https://github.com/ipython/ipykernel/pull/431))
* Moved `InProcessKernelClient.flush` to `DummySocket` ([#437](https://github.com/ipython/ipykernel/pull/437))
* Don\'t redirect stdout if nose machinery is not present ([#427](https://github.com/ipython/ipykernel/pull/427))
* Rename `_asyncio.py` to
    `_asyncio_utils.py` to avoid name conflicts on Python
    3.6+ ([#426](https://github.com/ipython/ipykernel/pull/426))
* Only generate kernelspec when installing or building wheel ([#425](https://github.com/ipython/ipykernel/pull/425))
* Fix priority ordering of control-channel messages in some cases
    [#443](https://github.com/ipython/ipykernel/pull/443)

### 5.1.2

5.1.2 fixes some socket-binding race conditions that caused testing
failures in nbconvert.

* Fix socket-binding race conditions ([#412](https://github.com/ipython/ipykernel/pull/412),
    [#419](https://github.com/ipython/ipykernel/pull/419))
* Add a no-op `flush` method to `DummySocket` and comply with stream
    API ([#405](https://github.com/ipython/ipykernel/pull/405))
* Update kernel version to indicate kernel v5.3 support ([#394](https://github.com/ipython/ipykernel/pull/394))
* Add testing for upcoming Python 3.8 and PEP 570 positional
    parameters ([#396](https://github.com/ipython/ipykernel/pull/396), [#408](https://github.com/ipython/ipykernel/pull/408))

### 5.1.1

5.1.1 fixes a bug that caused cells to get stuck in a busy state.

* Flush after sending replies [#390](https://github.com/ipython/ipykernel/pull/390)

### 5.1.0

5.1.0 fixes some important regressions in 5.0, especially on Windows.

[5.1.0 on GitHub](https://github.com/ipython/ipykernel/milestones/5.1)

* Fix message-ordering bug that could result in out-of-order
    executions, especially on Windows [#356](https://github.com/ipython/ipykernel/pull/356)
* Fix classifiers to indicate dropped Python 2 support
    [#354](https://github.com/ipython/ipykernel/pull/354)
* Remove some dead code [#355](https://github.com/ipython/ipykernel/pull/355)
* Support rich-media responses in `inspect_requests` (tooltips)
    [#361](https://github.com/ipython/ipykernel/pull/361)

## 5.0

### 5.0.0

[5.0.0 on GitHub](https://github.com/ipython/ipykernel/milestones/5.0)

* Drop support for Python 2. `ipykernel` 5.0 requires Python \>= 3.4
* Add support for IPython\'s asynchronous code execution
    [#323](https://github.com/ipython/ipykernel/pull/323)
* Update release process in `CONTRIBUTING.md` [#339](https://github.com/ipython/ipykernel/pull/339)

## 4.10

[4.10 on GitHub](https://github.com/ipython/ipykernel/milestones/4.10)

* Fix compatibility with IPython 7.0 [#348](https://github.com/ipython/ipykernel/pull/348)
* Fix compatibility in cases where sys.stdout can be None
    [#344](https://github.com/ipython/ipykernel/pull/344)

## 4.9

### 4.9.0

[4.9.0 on GitHub](https://github.com/ipython/ipykernel/milestones/4.9)

* Python 3.3 is no longer supported [#336](https://github.com/ipython/ipykernel/pull/336)
* Flush stdout/stderr in KernelApp before replacing
    [#314](https://github.com/ipython/ipykernel/pull/314)
* Allow preserving stdout and stderr in KernelApp
    [#315](https://github.com/ipython/ipykernel/pull/315)
* Override writable method on OutStream [#316](https://github.com/ipython/ipykernel/pull/316)
* Add metadata to help display matplotlib figures legibly
    [#336](https://github.com/ipython/ipykernel/pull/336)

## 4.8

### 4.8.2

[4.8.2 on GitHub](https://github.com/ipython/ipykernel/milestones/4.8.2)

* Fix compatibility issue with qt eventloop and pyzmq 17
    [#307](https://github.com/ipython/ipykernel/pull/307).

### 4.8.1

[4.8.1 on GitHub](https://github.com/ipython/ipykernel/milestones/4.8.1)

* set zmq.ROUTER_HANDOVER socket option when available to workaround
    libzmq reconnect bug [#300](https://github.com/ipython/ipykernel/pull/300).
* Fix sdists including absolute paths for kernelspec files, which
    prevented installation from sdist on Windows
    [#306](https://github.com/ipython/ipykernel/pull/306).

### 4.8.0

[4.8.0 on GitHub](https://github.com/ipython/ipykernel/milestones/4.8)

* Cleanly shutdown integrated event loops when shutting down the
    kernel. [#290](https://github.com/ipython/ipykernel/pull/290)
* `%gui qt` now uses Qt 5 by default rather than Qt 4, following a
    similar change in terminal IPython. [#293](https://github.com/ipython/ipykernel/pull/293)
* Fix event loop integration for `asyncio` when run with Tornado 5, which uses asyncio where
    available. [#296](https://github.com/ipython/ipykernel/pull/296)

## 4.7

### 4.7.0

[4.7.0 on GitHub](https://github.com/ipython/ipykernel/milestones/4.7)

* Add event loop integration for `asyncio`.
* Use the new IPython completer API.
* Add support for displaying GIF images (mimetype `image/gif`).
* Allow the kernel to be interrupted without killing the Qt console.
* Fix `is_complete` response with cell magics.
* Clean up encoding of bytes objects.
* Clean up help links to use `https` and improve display titles.
* Clean up ioloop handling in preparation for tornado 5.

## 4.6

### 4.6.1

[4.6.1 on GitHub](https://github.com/ipython/ipykernel/milestones/4.6.1)

* Fix eventloop-integration bug preventing Qt windows/widgets from
    displaying with ipykernel 4.6.0 and IPython ≥ 5.2.
* Avoid deprecation warnings about naive datetimes when working with
    jupyter_client ≥ 5.0.

### 4.6.0

[4.6.0 on GitHub](https://github.com/ipython/ipykernel/milestones/4.6)

* Add to API `DisplayPublisher.publish` two new fully
    backward-compatible keyword-args:

    > * `update: bool`
    > * `transient: dict`

* Support new `transient` key in
    `display_data` messages spec for `publish`.
    For a display data message, `transient` contains data
    that shouldn\'t be persisted to files or documents. Add a
    `display_id` to this `transient` dict by
    `display(obj, display_id=\...)`

* Add `ipykernel_launcher` module which removes the
    current working directory from `sys.path` before
    launching the kernel. This helps to reduce the cases where the
    kernel won\'t start because there\'s a `random.py` (or
    similar) module in the current working directory.

* Add busy/idle messages on IOPub during processing of aborted
    requests

* Add active event loop setting to GUI, which enables the correct
    response to IPython\'s `is_event_loop_running_xxx`

* Include IPython kernelspec in wheels to reduce reliance on \"native
    kernel spec\" in jupyter_client

* Modify `OutStream` to inherit from
    `TextIOBase` instead of object to improve API support
    and error reporting

* Fix IPython kernel death messages at start, such as \"Kernel
    Restarting\...\" and \"Kernel appears to have died\", when
    parent-poller handles PID 1

* Various bugfixes

## 4.5

### 4.5.2

[4.5.2 on GitHub](https://github.com/ipython/ipykernel/milestones/4.5.2)

* Fix bug when instantiating Comms outside of the IPython kernel
    (introduced in 4.5.1).

### 4.5.1

[4.5.1 on GitHub](https://github.com/ipython/ipykernel/milestones/4.5.1)

* Add missing `stream` parameter to overridden
    `getpass`
* Remove locks from iopub thread, which could cause deadlocks during
    debugging
* Fix regression where KeyboardInterrupt was treated as an aborted
    request, rather than an error
* Allow instantiating Comms outside of the IPython kernel

### 4.5.0

[4.5 on GitHub](https://github.com/ipython/ipykernel/milestones/4.5)

* Use figure.dpi instead of savefig.dpi to set DPI for inline figures
* Support ipympl matplotlib backend (requires IPython update as well
    to fully work)
* Various bugfixes, including fixes for output coming from threads,
    and `input` when called with
    non-string prompts, which stdlib allows.

## 4.4

### 4.4.1

[4.4.1 on GitHub](https://github.com/ipython/ipykernel/milestones/4.4.1)

* Fix circular import of matplotlib on Python 2 caused by the inline
    backend changes in 4.4.0.

### 4.4.0

[4.4.0 on GitHub](https://github.com/ipython/ipykernel/milestones/4.4)

* Use
    [MPLBACKEND](http://matplotlib.org/devel/coding_guide.html?highlight=mplbackend#developing-a-new-backend)
    environment variable to tell matplotlib \>= 1.5 use use the inline
    backend by default. This is only done if MPLBACKEND is not already
    set and no backend has been explicitly loaded, so setting
    `MPLBACKEND=Qt4Agg` or calling `%matplotlib notebook` or
    `matplotlib.use('Agg')` will take precedence.
* Fixes for logging problems caused by 4.3, where logging could go to
    the terminal instead of the notebook.
* Add `--sys-prefix` and `--profile` arguments to
    `ipython kernel install`.
* Allow Comm (Widget) messages to be sent from background threads.
* Select inline matplotlib backend by default if `%matplotlib` magic
    or `matplotlib.use()` are not called explicitly (for matplotlib \>=
    1.5).
* Fix some longstanding minor deviations from the message protocol
    (missing status: ok in a few replies, connect_reply format).
* Remove calls to NoOpContext from IPython, deprecated in 5.0.

## 4.3

### 4.3.2

* Use a nonempty dummy session key for inprocess kernels to avoid
    security warnings.

### 4.3.1

* Fix Windows Python 3.5 incompatibility caused by faulthandler patch
    in 4.3

### 4.3.0

[4.3.0 on GitHub](https://github.com/ipython/ipykernel/milestones/4.3)

* Publish all IO in a thread, via `IOPubThread`. This solves the problem of requiring
    `sys.stdout.flush` to be called in
    the notebook to produce output promptly during long-running cells.
* Remove references to outdated IPython guiref in kernel banner.
* Patch faulthandler to use `sys.__stderr__` instead of forwarded
    `sys.stderr`, which has no fileno when forwarded.
* Deprecate some vestiges of the Big Split:
    * `ipykernel.find_connection_file`
        is deprecated. Use
        `jupyter_client.find_connection_file` instead.

    \- Various pieces of code specific to IPython parallel are
    deprecated in ipykernel and moved to ipyparallel.

## 4.2

### 4.2.2

[4.2.2 on GitHub](https://github.com/ipython/ipykernel/milestones/4.2.2)

* Don\'t show interactive debugging info when kernel crashes
* Fix handling of numerical types in json_clean
* Testing fixes for output capturing

### 4.2.1

[4.2.1 on GitHub](https://github.com/ipython/ipykernel/milestones/4.2.1)

* Fix default display name back to \"Python X\" instead of \"pythonX\"

### 4.2.0

[4.2 on GitHub](https://github.com/ipython/ipykernel/milestones/4.2)

* Support sending a full message in initial opening of comms
    (metadata, buffers were not previously allowed)
* When using `ipython kernel install --name` to install the IPython
    kernelspec, default display-name to the same value as `--name`.

## 4.1

### 4.1.1

[4.1.1 on GitHub](https://github.com/ipython/ipykernel/milestones/4.1.1)

* Fix missing `ipykernel.__version__` on Python 2.
* Fix missing `target_name` when opening comms from the frontend.

### 4.1.0

[4.1 on GitHub](https://github.com/ipython/ipykernel/milestones/4.1)

* add `ipython kernel install` entrypoint for installing the IPython
    kernelspec
* provisional implementation of `comm_info` request/reply for msgspec
    v5.1

## 4.0

[4.0 on GitHub](https://github.com/ipython/ipykernel/milestones/4.0)

4.0 is the first release of ipykernel as a standalone package.
