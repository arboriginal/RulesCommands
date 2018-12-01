# RulesCommands

RulesCommands is an addon for [Rules Spigot plugin](https://www.spigotmc.org/resources/rules.56584/) (for now, it needs [my fork](https://github.com/arboriginal/Rules), I've send [a PR](https://github.com/KlasterStormInc/Rules/pull/1) to the author). It use the API of this plugin to react to accept / deny rules decision of the player, by executing commands you set in your config file.

## Requirements

As said in introduction, you need to use my version of Rules plugin, except if the author as integrated my additions in his own release. Nothing else, except plugins you actually have on your server from which commands you set are.

## Installation

Simply copy [RulesCommands.jar](https://github.com/arboriginal/RulesCommands/releases) into your "plugins" directory, then start the server. A new folder containing the default `config.yml` is automatically generated.

**YOU SHOULD EDIT** (or verify) the config.yml generated, because it's an example and it will not work on your server if you don't have same jails (essentials) and groups (groupmanager) than me. So, configure commands you want to trigger, then type `/rulescmds-reload`.

## Commands

* **rulescmds-reload**: Reload the configuration.

## Permissions

* **rulescmds-reload**: Allows to use `/rulescmds-reload` command.

## Configuration

Edit `plugins/RulesCommands/config.yml` then reload the plugin, or the server if you prefer (it also works well with [plugman](https://dev.bukkit.org/projects/plugman)). All parameters are explained in the [config.yml from the source code](https://github.com/arboriginal/RulesCommands/blob/master/src/config.yml).

