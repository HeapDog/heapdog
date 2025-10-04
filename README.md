## Welcome to HeapDog


### Project setup guide for development
#### Using devcontainer
1. Install [Visual Studio Code](https://code.visualstudio.com/)
2. Install the [Remote - Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)
3. Open the project folder in VS Code
4. Click on the green button in the bottom left corner of the window
5. Select "Reopen in Container"
6. Wait for the container to build and start
7. Open a terminal in VS Code (Terminal > New Terminal)
8. Run the following command to start the development server:
   ```
   gradle bootRun
   ```

#### IntelliJ IDEA
TBD
### Recommended: Using DevPods
#### We have first class support with devpods, check out the [devpods documentation](https://devpods.dev/docs/getting-started/installation) to get started.
0. Clone this repository
1. Install [DevPods](https://devpods.dev/docs/getting-started/installation)
2. Create a new devpod with the following command:
   ```
   devpod up . --id heapdog-intellij --provider docker --ide intellij
   ```
*NB: This should take a while as it will build the docker image and inject IDE backend.*

Or with VS Code:
```
devpod up . --id heapdog-vscode --provider docker --ide vscode
```