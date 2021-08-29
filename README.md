# registry-regulations-validation-cli

```bash
Usage: java -jar registry-regulations-validator-cli.jar
       [--help] 
       [--bp-auth-files=<arg>] [--bp-trembita-files=<arg>]
       [--bpmn-files=<arg>] [--dmn-files=<arg>] [--form-files=<arg>]
       [--global-vars-files=<arg>] [--roles-files=<arg>]
Options:
    --help                     Help on utility usage
    --bp-auth-files=<arg>      BP authorization regulation files (accepts
                               multiple values separated by ',')
    --bp-trembita-files=<arg>   BP Trembita configuration regulation files
    --bpmn-files=<arg>         Business processes regulation files (accepts multiple values separated by ',')
                               (accepts multiple values separated by ',')
    --dmn-files=<arg>          Business rules regulation files (accepts
                               multiple values separated by ',')
    --form-files=<arg>         UI forms regulation files (accepts multiple
                               values separated by ',')
    --global-vars-files=<arg>   Global variables regulation files (accepts multiple values separated by ',')
    --roles-files=<arg>        Authorization roles regulation files
                               (accepts multiple values separated by ',')
Exit codes: 0 (success), 1 (system error), 10 (validation failure)
```