from flask import Flask, jsonify, request
import mysql.connector

app = Flask(__name__)


def get_db_connection():
    # Trocar os dados abaixo com o usuário, senha e banco de dados que você criou
    return mysql.connector.connect(
        host="localhost", user="root", password="", database="mist"
    )


# Métodos do plataforma
@app.route("/plataformas", methods=["GET"])
def get_plataformas():
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute(
        """
                   SELECT console.id  
     , console.nome
     , console.preco
     , console.ano
     , cor.id AS cor_id
     , cor.cor AS cor_cor
     , marca.id AS marca_id
     , marca.marca AS marca_marca
FROM console, cor, marca
WHERE console.cor_id = cor.id
  AND console.marca_id = marca.id"""
    )
    plataformas = cursor.fetchall()
    for plataforma in plataformas:
        plataforma["cor"] = {"id": plataforma["cor_id"], "cor": plataforma["cor_cor"]}
        plataforma["marca"] = {
            "id": plataforma["marca_id"],
            "marca": plataforma["marca_marca"],
        }
        del plataforma["cor_id"]
        del plataforma["cor_cor"]
        del plataforma["marca_id"]
        del plataforma["marca_marca"]

    cursor.close()
    conn.close()
    return jsonify(plataformas)


@app.route("/plataforma/<int:plataforma_id>", methods=["GET"])
def get_plataforma(plataforma_id):
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM console WHERE id = %s", (plataforma_id,))
    plataforma = cursor.fetchone()
    cursor.close()
    conn.close()
    if plataforma:
        return jsonify(plataforma)
    else:
        return jsonify({"error": "plataforma not found"}), 404


@app.route("/plataforma", methods=["POST"])
def insert_plataforma():
    data = request.get_json()
    nome = data.get("nome")
    preco = data.get("preco")
    marca = data.get("marca")
    marca_id = marca["id"]
    cor = data.get("cor")
    cor_id = cor["id"]
    ano = data.get("ano")
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute(
        "INSERT INTO `console`(`nome`, `preco`, `marca_id`, `cor_id`, `ano`) VALUES (%s,%s,%s,%s,%s)",
        (nome, preco, marca_id, cor_id, ano),
    )
    conn.commit()
    new_id = cursor.lastrowid
    cursor.close()

    cursor = conn.cursor(dictionary=True)

    cursor.execute(
        """
    SELECT console.id  
         , console.nome
        , console.preco
        , console.ano
        , cor.id AS cor_id
        , cor.cor AS cor_cor
        , marca.id AS marca_id
        , marca.marca AS marca_marca
    FROM console, cor, marca
    WHERE console.cor_id = cor.id
    AND console.marca_id = marca.id AND console.id = %s""",
        (new_id,),
    )

    plataforma = cursor.fetchone()
    conn.close()

    plataforma["cor"] = {"id": plataforma["cor_id"], "cor": plataforma["cor_cor"]}
    plataforma["marca"] = {
        "id": plataforma["marca_id"],
        "marca": plataforma["marca_marca"],
    }
    del plataforma["cor_id"]
    del plataforma["cor_cor"]
    del plataforma["marca_id"]
    del plataforma["marca_marca"]

    return jsonify(plataforma)


@app.route("/plataforma/<int:plataforma_id>", methods=["DELETE"])
def delete_plataforma(plataforma_id):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("DELETE FROM console WHERE id = %s", (plataforma_id,))
    conn.commit()
    cursor.close()
    conn.close()

    if cursor.rowcount == 0:
        return jsonify({"error": "plataforma not found"}), 404
    return jsonify({"message": "plataforma deleted"})


@app.route("/plataforma/<int:plataforma_id>", methods=["PUT"])
def update_plataforma(plataforma_id):
    data = request.get_json()
    nome = data.get("nome")
    preco = data.get("preco")
    marca = data.get("marca")
    cor = data.get("cor")
    ano = data.get("ano")
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute(
        "UPDATE `console` SET `nome`= %s,`preco`=%s,`marca_id`= %s,`cor_id`= %s ,`ano`= %s WHERE id = %s",
        (nome, preco, marca['id'], cor['id'], ano, plataforma_id,),
    )
    conn.commit()
    cursor.close()

    cursor = conn.cursor(dictionary=True)

    cursor.execute(
        """
    SELECT console.id  
         , console.nome
        , console.preco
        , console.ano
        , cor.id AS cor_id
        , cor.cor AS cor_cor
        , marca.id AS marca_id
        , marca.marca AS marca_marca
    FROM console, cor, marca
    WHERE console.cor_id = cor.id
    AND console.marca_id = marca.id AND console.id = %s""",
        (plataforma_id,),
    )


    plataforma = cursor.fetchone()
    conn.close()

    plataforma["cor"] = {"id": plataforma["cor_id"], "cor": plataforma["cor_cor"]}
    plataforma["marca"] = {
        "id": plataforma["marca_id"],
        "marca": plataforma["marca_marca"],
    }
    del plataforma["cor_id"]
    del plataforma["cor_cor"]
    del plataforma["marca_id"]
    del plataforma["marca_marca"]

    return jsonify(plataforma)


# Métodos do Jogo
@app.route("/jogos", methods=["GET"])
def get_jogos():
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM jogo")
    plataforma = cursor.fetchall()
    cursor.close()
    conn.close()
    return jsonify(plataforma)


@app.route("/jogo/<int:jogo_id>", methods=["GET"])
def get_jogo(jogo_id):
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM jogo WHERE id = %s", (jogo_id))
    plataforma = cursor.fetchone()
    cursor.close()
    conn.close()
    if plataforma:
        return jsonify(plataforma)
    else:
        return jsonify({"error": "Jogo not found"}), 404


@app.route("/jogo", methods=["POST"])
def insert_jogo():
    data = request.get_json()
    nome = data.get("nome")
    preco = data.get("preco")
    ano = data.get("ano")
    capa = data.get("capa")
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute(
        "INSERT INTO `jogo`(`nome`, `preco`, `ano`, `capa`) VALUES (%s,%s,%s,%s)",
        (nome, preco, ano, capa),
    )
    conn.commit()
    new_id = cursor.lastrowid
    cursor.close()
    conn.close()

    return jsonify({"id": new_id, "message": "Jogo created"}), 201


@app.route("/jogo/<int:jogo_id>", methods=["DELETE"])
def delete_jogo(jogo_id):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("DELETE FROM jogo WHERE id = %s", (jogo_id,))
    conn.commit()
    cursor.close()
    conn.close()

    if cursor.rowcount == 0:
        return jsonify({"error": "Jogo not found"}), 404
    return jsonify({"message": "Jogo deleted"})


@app.route("/jogo/<int:jogo_id>", methods=["PUT"])
def update_jogo(jogo_id):
    data = request.get_json()
    nome = data.get("nome")
    preco = data.get("preco")
    ano = data.get("ano")
    capa = data.get("capa")
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute(
        "UPDATE `jogo` SET `nome`= %s,`preco`=%s,`ano`= %s, `capa`= %s WHERE id = %s",
        (nome, preco, ano, capa, jogo_id),
    )
    conn.commit()
    cursor.close()
    conn.close()

    if cursor.rowcount == 0:
        return jsonify({"error": "plataforma not found"}), 404
    return jsonify({"message": "plataforma updated"})

#Cores
@app.route("/cor",methods = ["POST"])
def insert_cor():
    data = request.get_json()
    nome = data.get("nome")
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute(
        "INSERT INTO `cor`(`cor`) VALUES (%s)",
        (nome),
    )
    conn.commit()
    new_id = cursor.lastrowid
    cursor.close()
    conn.close()

    return jsonify({"id": new_id, "message": "Cor created"}), 201

@app.route("/cor/<int:cor_id>", methods=["GET"])
def get_cor(cor_id):
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM cor WHERE id = %s", (cor_id))
    plataforma = cursor.fetchone()
    cursor.close()
    conn.close()
    if plataforma:
        return jsonify(plataforma)
    else:
        return jsonify({"error": "Cor not found"}), 404

@app.route("/cores", methods=["GET"])
def get_cores():
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM cor")
    cor= cursor.fetchall()
    cursor.close()
    conn.close()
    return jsonify(cor)

#Marca
@app.route("/marca",methods = ["POST"])
def insert_marca():
    data = request.get_json()
    nome = data.get("nome")
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute(
        "INSERT INTO `marca`(`marca`) VALUES (%s)",
        (nome),
    )
    conn.commit()
    new_id = cursor.lastrowid
    cursor.close()
    conn.close()

    return jsonify({"id": new_id, "message": "Cor created"}), 201

@app.route("/marca/<int:marca_id>", methods=["GET"])
def get_marca(marca_id):
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM marca WHERE id = %s", (marca_id))
    plataforma = cursor.fetchone()
    cursor.close()
    conn.close()
    if plataforma:
        return jsonify(plataforma)
    else:
        return jsonify({"error": "Cor not found"}), 404

@app.route("/marcas", methods=["GET"])
def get_marcas():
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM marca")
    cor= cursor.fetchall()
    cursor.close()
    conn.close()
    return jsonify(cor)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
