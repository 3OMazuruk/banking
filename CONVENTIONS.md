## Code of Conduct
This project is governed by the [Java of Conduct](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html). By participating you are expected to uphold this code.

## How to Contribute
### Ask questions & Issue Lifecycle

Reporting an issue or making a feature request is a great way to contribute. Your feedback and the conversations that result from it provide a continuous flow of ideas.

If you create an issue after a discussion on Stack Overflow, please provide a description in the issue instead of simply referring to Stack Overflow. The issue tracker is an important place of record for design discussions and should be self-sufficient.

Once you're ready, create an issue on [GitHub](https://github.com/3OMazuruk/banking/issues).

Many issues are caused by subtle behavior, typos, and unintended configuration. Creating a [Minimal Reproducible Example](https://stackoverflow.com/help/minimal-reproducible-example) of the problem helps the team quickly triage your issue and get to the core of the problem.

### Git Commit Guidelines
Each commit message starts with a type, a scope, and a subject.

Below that, the commit message has a body.

- [type](#Type): what type of change this commit contains.
- [scope](#Scope): what item of code this commit is changing.
- [subject](#Subject): a short description of the changes.
- [body](#Body)(optional): a more in-depth description of the changes

Examples:

`feat(ruler): add inches as well as centimeters`

`fix(protractor): fix 90 degrees counting as 91 degrees`
```
refactor(pencil): use graphite instead of lead

Closes #640.

Graphite is a much more available resource than lead, so we use it to lower the price.
```

Any line of the commit message should not be longer 100 characters. This allows the message to be easier to read on github as well as in various git tools.

#### Type
Is recommended to be one of the below items. Only feat and fix show up in the changelog, in addition to breaking changes (see breaking changes section at bottom).
- **feat**: A new feature
- **fix**: A bug fix
- **docs**: Documentation only changes
- **style**: Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)
- **refactor**: A code change that neither fixes a bug or adds a feature
- **test**: Adding missing tests
- **chore**: Changes to the build process or auxiliary tools and libraries such as documentation generation

#### Scope
The scope could be anything specifying place of the commit change. For example `SecuritySetting`, `UserController`

#### Subject
The subject contains succinct description of the change:

- use the imperative, present tense: "change" not "changed" nor "changes"
- don't capitalize first letter
- no dot (.) at the end

### Submit a Pull Request

- Should you create an issue first? No, just create the pull request and use the description to provide context and motivation, as you would for an issue. If you want to start a discussion first or have already created an issue, once a pull request is created, we will close the issue as superseded by the pull request, and the discussion about the issue will continue under the pull request.
- Always check out the `main` branch and submit pull requests against it
- Choose the granularity of your commits consciously and squash commits that represent multiple edits or corrections of the same logical change.
- If there is a prior issue, reference the GitHub issue number in the description of the pull request.

If accepted, your contribution may be heavily modified as needed prior to merging. You will likely retain author attribution for your Git commits granted that the bulk of your changes remain intact. You may also be asked to rework the submission.

If asked to make corrections, simply push the changes against the same branch, and your pull request will be updated. In other words, you do not need to create a new pull request when asked to make changes.

### Build from Source
See the [Build from Source](https://github.com/3OMazuruk/banking/wiki/Build-from-Source) wiki page for instructions on how to check out, build, and import the Spring Framework source code into your IDE.

### Source Code Style
The wiki pages [Code Style](https://github.com/spring-projects/spring-framework/wiki/Code-Style) and [IntelliJ IDEA Editor Settings](https://github.com/spring-projects/spring-framework/wiki/IntelliJ-IDEA-Editor-Settings) define the source file coding standards we use along with some IDEA editor settings we customize.