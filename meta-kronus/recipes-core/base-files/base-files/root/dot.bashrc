# ~/.bashrc: executed by bash(1) for non-login shells.

export PS1='\[\e[31m\]\h \[\e[33m\]\W \$\[\e[0m\] '

# Colorize `ls` command
export LS_OPTIONS='--color=auto'
eval `dircolors`
alias ls='ls $LS_OPTIONS'
alias ll='ls $LS_OPTIONS -l'
alias l='ls $LS_OPTIONS -lA'

# Set locale
export LC_ALL=en_US.UTF-8
export LANG=en_US.UTF-8
export LANGUAGE=en_US.UTF-8
