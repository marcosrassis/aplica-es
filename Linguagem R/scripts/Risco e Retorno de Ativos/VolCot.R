### Script criado por Marcos Assis
### Finalidade: 
###   Obter as cota��es de ativos negociados na B3 em linguage R.
###   Calcular os retornos m�dios e as volatilidades dos ativos
###   Gerar um arquivo PDF do gr�fico do Risco x Retorno dos ativos

### Nome do script: VolCot.R


### Pacotes que ser�o necess�rios para os gr�ficos e depois para manipular os dados
library(rvest) # for scraping
library(tidyverse) # blanket import for core tidyverse packages
library(ggthemes)
library(tidyquant) # for tidy financial analysis
library(janitor) # tidy data cleaning functions

# Remove todos os objetos que foram criados e limpa a mem�ria com a fun��o gc()
rm(list = ls())
gc()

# Vari�vel contendo o local do diret�rio de trabalho
path_app <- "D://Marcos//Developer//Aplica��es//Linguagem R//scripts//Risco e Retorno de Ativos"

# Configurando o diret�rio de trabalho
setwd(file.path(path_app))

# Oculta os warnings
options("getSymbols.warning4.0" = F,
        "getSymbols.auto.assign" = F)

# Ativos para serem analisados
ativos <- c("petr4",
            "wege3",
            "mrve3",
            "card3",
            "enat3",
            "itsa4",
            "vivt4",
            "fesa4",
            "linx3",
            "brsr6",
            "trpl4",
            "pssa3",
            "egie3",
            "timp3",
            "prio3",
            "beef3",
            "movi3",
            "b3sa3",
            "lcam3",
            "mglu3",
            "flry3",
            "tgma3",
            "csan3",
            "sapr4",
            "grnd3",
            "cmig4",
            "slce3",
            "ecor3",
            "usim5",
            "alup4",
            "fras3",
            "enbr3",
            "hype3",
            "mypk3",
            "odpv3",
            "goau4",
            "taee11")

# Busca os dados de todas as a��es do vetor ativos
getSymbols(paste(ativos, ".sa", sep=""), auto.assign=TRUE) 

# Cria um vetor somente com c�digos v�lidos de a��es negociadas na bolsa 
tickers <- ls(pattern= ".SA", all.names = TRUE)    

# Cria um data frame com todos os ativos v�lidos e suas respectivas cota��es
tickers_df <- tq_get(tickers, na.rm = TRUE)

# Calcula o retorno di�rio dos ativos 
daily_sector = tickers_df %>% group_by(symbol) %>% 
  tq_transmute(select     = adjusted, 
               mutate_fun = periodReturn, 
               period     = "daily") %>% 
  ungroup()

# Renomeia o cabe�alho do objeto
names(daily_sector)[3] <- "return"

# Seleciona apenas as colunas necess�rias
return_and_vol <- select(daily_sector, symbol, return) %>% mutate(return = return * 100)

# Calcula o retorno m�dio e a volatilade dos ativos
return_and_vol <- group_by(return_and_vol, symbol) %>% 
  dplyr::summarise(avg_return = round(mean(return), 2), Volatility = round(sd(return) * 100,2), Sharper = round((mean(return) / sd(return)) * 100, 2)) %>% 
  arrange(desc(avg_return), desc(Volatility), desc(Sharper)) %>%
  as.data.frame()

# Cria pdf
pdf('isharpe_cot.pdf')

# Gera o gr�fico dos ativos de maior retorno 
return_and_vol %>% ggplot(aes(reorder(symbol, -avg_return), avg_return, fill = avg_return))+
  geom_col()+
  coord_flip()+
  labs(title = "Rentabilidade", x = "Pap�is", y = "Retorno m�dio")+
  theme_classic()+
  theme(legend.position="none")

# Gera o gr�fico do �ndice sharper dos ativos 
new_return_and_vol <- return_and_vol %>% arrange(desc(Sharper))
new_return_and_vol %>% ggplot(aes(x = reorder(symbol, -Sharper), y = Sharper, fill = Sharper))+
  geom_col()+
  coord_flip()+
  labs(title = "", x = "Pap�is", y = "�ndice Sharpe")+
  theme_classic()+
  theme(legend.position="none")

# Gera o gr�fico que relaciona o retorno m�dio e a volatilidade de cada ativo
plot = return_and_vol %>% ggplot(aes(avg_return, Volatility))+
  geom_text(aes(label = symbol), size = 3)+
  labs(title = "�ndice Sharpe", x = "Retorno", y = "Risco", subtitle = "Fonte: Yahoo Finance")+
  theme_minimal()

plot

dev.off() 

# Renomeia os cabe�alhos do objeto
names(new_return_and_vol)[2] <- "avg"
names(new_return_and_vol)[3] <- "vol"
names(new_return_and_vol)[4] <- "isharpe"

# Retira o sufixo '.SA' do ticker dos ativos
new_return_and_vol$symbol <- str_replace(new_return_and_vol$symbol,".SA$","")

# Salva o resultado em arquivo CSV
write.csv(new_return_and_vol,paste(path_app, "isharpe_cot.csv", sep = ""), row.names = FALSE)
