# EyesOnRoad — aplicação Python

Protótipo acadêmico de visão computacional que usa a webcam para estimar sinais visuais associados à sonolência. O programa acompanha a abertura dos olhos, identifica possíveis bocejos e **reproduz um alarme sonoro pulsante e mais intenso quando os olhos são classificados como fechados por 2,5 segundos consecutivos**. Também usa os pontos faciais detectados pelo **MediaPipe** para estimar a inclinação lateral da cabeça. Para evitar alertas a cada movimento, calibra a posição inicial por 2 segundos, suaviza os ângulos e só alerta se a cabeça ficar fortemente inclinada para um dos lados por cerca de 1 segundo. Inclinar a cabeça para frente, por si só, não dispara esse alerta. A sirene dura no máximo 2,5 segundos e ocorre uma vez por episódio; só rearma depois que a cabeça volta à posição neutra por 1 segundo.

## O que está incluído

- `main.py`: aplicação de monitoramento em tempo real.
- `requirements.txt`: bibliotecas necessárias, incluindo `mediapipe`, `opencv-python` e `numpy`.
- `configurar.bat`: cria um ambiente virtual e instala as bibliotecas.
- `iniciar.bat`: inicia o monitoramento.
- `testar_som.bat` e `testar_som.py`: permitem testar o áudio antes de abrir a webcam.
- `sons/alarme_forte.wav`: alarme pulsante com tons alternados e harmônicos, reproduzido pelo programa e pelo teste de som.
- `dados/alertas.csv`: será criado automaticamente para registrar eventos.
- `modelos/face_landmarker.task`: será baixado automaticamente na primeira execução.

## Requisitos

- Windows 10 ou 11 (os arquivos `.bat` foram preparados para Windows).
- Python 64 bits instalado e o comando `py` disponível no Prompt de Comando.
- Webcam conectada e permissão do Windows para usá-la.
- Conexão com a internet para instalar as bibliotecas e baixar o modelo facial.
- Saída de áudio habilitada para ouvir o alarme.

## Instalação passo a passo no Windows

### 1. Baixe e extraia

Baixe o arquivo ZIP do EyesOnRoad e extraia **todos os arquivos** para uma pasta fácil de localizar, como `Documentos\EyesOnRoad_Python`. Não execute os arquivos de dentro da visualização compactada do ZIP.

### 2. Verifique o Python

Abra o menu Iniciar, procure por `Prompt de Comando` e execute:

```bat
py --version
```

Se aparecer uma versão do Python, pode continuar. Se aparecer que o comando não foi encontrado, instale o Python para Windows pelo site oficial `https://www.python.org/downloads/windows/` e, durante a instalação, habilite a opção de adicionar o Python ao PATH. Feche e reabra o Prompt de Comando depois de instalar.

### 3. Instale as dependências

Entre na pasta que você extraiu e dê dois cliques em `configurar.bat`. Uma janela preta vai abrir, criar o ambiente virtual `.venv` e instalar OpenCV, MediaPipe e NumPy. Mantenha a internet conectada e espere a mensagem informando que o ambiente foi configurado. Esta etapa normalmente só precisa ser feita uma vez.

### 4. Teste o som (recomendado)

Dê dois cliques em `testar_som.bat`. Você deverá ouvir uma sirene curta, pulsante e com tons alternados. Caso não ouça, confira o volume do Windows, a saída de áudio selecionada e se o computador não está no mudo. O arquivo de áudio usa níveis digitais fortes; comece com o volume em nível moderado, especialmente se usar fones, e ajuste conforme necessário.

### 5. Inicie o monitoramento

Dê dois cliques em `iniciar.bat`. Na primeira execução, o aplicativo baixa o modelo facial do MediaPipe; isso requer internet. Depois, uma janela com o vídeo da webcam deverá abrir.

Mantenha o rosto visível, com boa iluminação. Durante os 2 primeiros segundos de rosto visível, mantenha a cabeça reta para calibrar a posição neutra. Ao reconhecer os olhos fechados por 2,5 segundos consecutivos, o programa tocará o alarme. Para a cabeça, só considera uma inclinação lateral forte (desvio de aproximadamente 35° em relação à calibração), suavizada ao longo dos quadros e persistente por cerca de 1 segundo. Inclinar a cabeça para frente não aciona o alerta de cabeça. Depois de alertar, a cabeça precisa retornar à posição neutra (até cerca de 18° do ponto de referência) por 1 segundo para que um novo alerta lateral possa ser acionado. O arquivo `sons/alarme_forte.wav` tem no máximo 2,5 segundos. Os ângulos são referências iniciais e podem precisar de calibração conforme a câmera, iluminação e características individuais. Para encerrar, selecione a janela do vídeo e pressione `Q` ou `Esc`.

## Executar pelo terminal ou VS Code

Abra um terminal na pasta do projeto e execute estes comandos, um de cada vez:

```bat
py -m venv .venv
.venv\Scripts\python.exe -m pip install --upgrade pip
.venv\Scripts\python.exe -m pip install -r requirements.txt
.venv\Scripts\python.exe main.py
```

Para testar apenas o som:

```bat
.venv\Scripts\python.exe testar_som.py
```

Se houver mais de uma câmera, tente usar a segunda:

```bat
.venv\Scripts\python.exe main.py --camera 1
```

## Registros de alertas

Os eventos ficam em `dados\alertas.csv`, que pode ser aberto no Excel. O arquivo registra data/hora, tipo de evento e medidas calculadas; não grava o vídeo nem armazena imagens.

Eventos registrados:

- `ALERTA: olhos fechados por 2.5 segundos`
- `ALERTA: inclinacao lateral persistente` (inclinação lateral acentuada e mantida)
- `Bocejo/sinal de boca aberta detectado`

## Ajustar o tempo e a sensibilidade

No começo de `main.py`, você encontrará:

```python
EYE_CLOSED_THRESHOLD = 0.20
MOUTH_OPEN_THRESHOLD = 0.35
EYES_CLOSED_SECONDS = 2.5
HEAD_SIDE_TILT_DEGREES = 35.0
HEAD_SIDE_HOLD_SECONDS = 1.0
HEAD_SIDE_NEUTRAL_DEGREES = 18.0
```

`EYES_CLOSED_SECONDS = 2.5` define o tempo para o alerta dos olhos. `HEAD_SIDE_TILT_DEGREES` define o ângulo lateral de início, `HEAD_SIDE_HOLD_SECONDS` define por quanto tempo a inclinação precisa persistir e `HEAD_SIDE_NEUTRAL_DEGREES` define o limite para considerar que a cabeça voltou ao neutro. A sirene WAV foi limitada a 2,5 segundos. Esses valores são referências heurísticas e podem precisar de ajustes conforme a câmera, iluminação e características individuais.

## Solução de problemas

**A câmera não abre**
- Feche Teams, Zoom, navegador ou qualquer outro aplicativo que possa estar usando a webcam.
- Verifique as permissões de câmera nas configurações de privacidade do Windows.
- Se houver outra câmera, tente o comando com `--camera 1`.

**O modelo não baixa**
- Confira a conexão com a internet e execute `iniciar.bat` novamente.
- Algumas redes podem bloquear o download do modelo.

**Erro ao instalar as bibliotecas**
- Confira a saída de `py --version`.
- Se aparecer erro, copie a mensagem completa da janela para diagnosticar. Não apague nem altere arquivos da instalação do Python para tentar corrigir.

**O som não toca**
- Execute `testar_som.bat`, aumente o volume do sistema e confira a saída de áudio selecionada.
- O programa reproduz `sons/alarme_forte.wav`. Confira se a pasta `sons` e o arquivo WAV continuam junto de `main.py`; se o arquivo faltar, o programa tenta usar bipes alternativos do Windows.

## Limitações importantes

Este é um protótipo acadêmico, não um dispositivo certificado para uso veicular. A detecção lateral é uma estimativa geométrica baseada nos pontos faciais do MediaPipe e no cálculo de pose do OpenCV; pode apresentar falsos positivos ou deixar de detectar inclinações por causa da iluminação, óculos, enquadramento, movimento do rosto ou posição da webcam. A calibração inicial pressupõe que a pessoa comece com a cabeça numa posição neutra. Movimentos bruscos e inclinações curtas devem ser filtrados, mas o comportamento ainda precisa ser validado na webcam real. Teste e ajuste os ângulos no ambiente controlado antes de uma demonstração. Não utilize o programa como único recurso para decidir se alguém está apto a dirigir. O processamento do vídeo é local. Este pacote não implementa cadastro/login, conexão com MySQL ou dashboard web; esses recursos precisam ser integrados separadamente ao backend da plataforma.
