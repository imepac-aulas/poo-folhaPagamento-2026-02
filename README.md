Uma instituição de ensino possui diferentes profissionais envolvidos em suas atividades acadêmicas e administrativas. Entre esses profissionais estão "professores", "coordenadores" e "bibliotecários."

Atualmente, a instituição precisa controlar mensalmente as informações relacionadas à remuneração desses profissionais. Cada tipo de funcionário possui uma forma específica de cálculo de sua remuneração, e determinados valores podem ser "acrescentados" ou "descontados" ao longo de cada mês.

A instituição deseja organizar esse processo por meio de um sistema de folha de pagamento que permita centralizar as informações, realizar os cálculos necessários e disponibilizar os resultados de forma organizada.

O sistema será utilizado pelos responsáveis pelo processamento da folha de pagamento da instituição.

----------

### Professor

A remuneração de um professor é determinada pela quantidade de aulas e pelo valor definido para cada aula.

### Coordenador

A remuneração de um coordenador considera:

* um valor-base;
* um percentual;
* a quantidade de alunos sob sua responsabilidade.

### Bibliotecário

A remuneração de um bibliotecário é determinada por um valor-base estabelecido pela instituição. 


----------
CLASSES: COORDENADOR, PROFESSOR, BIBLIOTECARIO, FOLHAPAGAMENTO, HOLERITE, ADITIVO, DESCONTO;

----------

## Descrição do fluxo de funcionamento do sistema

Antes de dar início a geração da folha de pagamento, o sistema deve permitir que o usuário cadastre os profissionais da instituição, incluindo suas informações pessoais e dados específicos relacionados à remuneração.

A seguir, o usuário poderá cadastrar os aditivos e descontos que serão aplicados aos profissionais durante o mês. Esses valores podem ser positivos (acréscimos) ou negativos (descontos) e devem ser associados a cada profissional.

Uma vez concluído o cadastro dos profissionais, aditivos e descontos, o sistema deve dar inicio ao processo de gerar a folha de pagamento e, durante a criação, deve-se criar o holerite de cada funcionario, com seus descontos e aditivos, valores bruto e liquido.

## Arquitetura do sistema 

O sistema sera contruído com uma camada de front(console - java), back e banco de dados. 
A camada de front será responsável por interagir com o usuário, permitindo o cadastro de profissionais, aditivos e descontos, bem como a geração da folha de pagamento. A camada de back será responsável por processar as informações, realizar os cálculos necessários e gerar os holerites. O banco de dados armazenará todas as informações relacionadas aos profissionais, aditivos, descontos e folhas de pagamento.
A camada de back deverá fazer as validações necessárias para garantir a integridade dos dados e a consistência das informações, bem como uma camada identificando as entidades do sistema, uma camada representando acesso ao banco de dados com DAO e uma camada de serviços para realizar as operações de negócio.
Além disso, o sistema deve ser projetado de forma modular, permitindo futuras expansões e melhorias.
