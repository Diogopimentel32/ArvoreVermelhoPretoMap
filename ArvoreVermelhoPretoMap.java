package ed2_atividade02;

import java.util.Set;

public class ArvoreVermelhoPretoMap<K extends Comparable<K>, V> implements MyMap<K, V>{

    private class No {
        private K key;
        private V value;
        private No esquerda;
        private No direita;
        private boolean cor;
        private int size;

        public No(K key, V val, boolean cor, int size) {
            this.key = key;
            this.value = val;
            this.cor = cor;
            this.size = size;
        }

        public K getKey() {
            return this.key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return this.value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public No getEsquerda() {
            return this.esquerda;
        }

        public void setEsquerda(No esquerda) {
            this.esquerda = esquerda;
        }

        public No getDireita() {
            return this.direita;
        }

        public void setDireita(No direita) {
            this.direita = direita;
        }

        public boolean getCor() {
            return this.cor;
        }

        public void setCor(boolean cor) {
            this.cor = cor;
        }

        public int getSize() {
            return this.size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

    private final boolean RED = true;
    private final boolean BLACK = false;
    private No raiz;

    @Override
    public int size() {
        return size(this.raiz);
    }

    private int size(No no) {
        if (no == null)
            return 0;

        return no.getSize();
    }

    @Override
    public boolean isEmpty() {
        return this.raiz == null;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null)
            return false;

        return get(key) != null;
    }

    @Override
    public V get(K key) {
        if (key == null)
            return null;

        No atual = this.raiz;

        while(atual != null) {
            int x = key.compareTo(atual.getKey());

            if (x < 0)
                atual = atual.getEsquerda();
            else if (x > 0)
                atual = atual.getDireita();
            else
                return atual.getValue();
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        if (key == null)
            return;
        if (value == null) {
            remove(key);
            return;
        }
         this.raiz = put(this.raiz, key, value);
        this.raiz.setCor(BLACK);
    }

    private No put (No no, K key, V value) {
        if (no == null)
            return new No(key, value, RED, 1);

        int x = key.compareTo(no.getKey());

        if (x < 0)
            no.setEsquerda(put(no.getEsquerda(), key, value));
        else if (x > 0)
            no.setDireita(put(no.getDireita(), key, value));
        else
            no.setValue(value);

        if (noVermelho(no.getDireita()) && !noVermelho(no.getEsquerda()))
            no = rotacionarEsquerda(no);
        if (noVermelho(no.getEsquerda()) && noVermelho(no.getEsquerda().getEsquerda()))
            no = rotacionarDireita(no);
        if (noVermelho(no.getEsquerda()) && noVermelho(no.getDireita()))
            alterarCor(no);

        no.setSize(1 + size(no.getEsquerda()) + size(no.getDireita()));
        return no;
    }

    @Override
    public void remove(K key) {
        if (key == null || !containsKey(key))
            return;
        if (!noVermelho(this.raiz.getEsquerda()) && !noVermelho(this.raiz.getDireita()))
            this.raiz.setCor(RED);

        this.raiz = remove(this.raiz, key);

        if (!isEmpty())
            this.raiz.setCor(BLACK);
    }

    private No remove(No no, K key) {
        if (key.compareTo(no.getKey()) < 0) {
            if (!noVermelho(no.getEsquerda()) && !noVermelho(no.getEsquerda().getEsquerda()))
                no = vermelhoPraEsquerda(no);

            no.setEsquerda(remove(no.getEsquerda(), key));
        }
        else {
            if (noVermelho(no.getEsquerda()))
                no = rotacionarDireita(no);
            if (key.compareTo(no.getKey()) == 0 && (no.getDireita() == null))
                return null;
            if (!noVermelho(no.getDireita()) && !noVermelho(no.getDireita().getEsquerda()))
                no = vermelhoPraDireita(no);
            if (key.compareTo(no.getKey()) == 0) {
                No aux = menor(no.getDireita());
                no.setKey(aux.getKey());
                no.setValue(aux.getValue());
                no.setDireita(removeMenor(no.getDireita()));
            }
            else
                no.setDireita(remove(no.getDireita(), key));
        }
        return retornarNo(no);
    }

    private No removeMenor(No no) {
        if (no.getEsquerda() == null)
            return null;
        if (!noVermelho(no.getEsquerda()) && !noVermelho(no.getEsquerda().getEsquerda()))
            no = vermelhoPraEsquerda(no);

        no.setEsquerda(removeMenor(no.getEsquerda()));
        return retornarNo(no);
    }

    private No menor(No no) {
        if (no.getEsquerda() == null)
            return no;

        return menor(no.getEsquerda());
    }

    @Override
    public void clear() {
        this.raiz = null;
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    private boolean noVermelho(No no) {
        if (no == null)
            return false;

        return no.getCor() == RED;
    }

    private No rotacionarDireita(No no) {
        No aux = no.getEsquerda();
        no.setEsquerda(aux.getDireita());
        aux.setDireita(no);
        aux.setCor(aux.getDireita().getCor());
        aux.getDireita().setCor(RED);
        aux.setSize(no.getSize());
        no.setSize(1 + size(no.getEsquerda()) + size(no.getDireita()));
        return aux;
    }

    private No rotacionarEsquerda(No no) {
        No aux = no.getDireita();
        no.setDireita(aux.getEsquerda());
        aux.setEsquerda(no);
        aux.setCor(aux.getEsquerda().getCor());
        aux.getEsquerda().setCor(RED);
        aux.setSize(no.getSize());
        no.setSize(1 + size(no.getEsquerda()) + size(no.getDireita()));
        return aux;
    }

    private void alterarCor(No no) {
        no.setCor(!no.getCor());
        no.getEsquerda().setCor(!no.getEsquerda().getCor());
        no.getDireita().setCor(!no.getDireita().getCor());
    }

    private No vermelhoPraDireita(No no) {
        alterarCor(no);
        if (noVermelho(no.getEsquerda().getEsquerda())) {
            no = rotacionarDireita(no);
            alterarCor(no);
        }
        return no;
    }

    private No vermelhoPraEsquerda(No no) {
        alterarCor(no);
        if (noVermelho(no.getDireita().getEsquerda())) {
            no.setDireita(rotacionarDireita(no.getDireita()));
            no = rotacionarEsquerda(no);
            alterarCor(no);
        }
        return no;
    }

    private No retornarNo(No no) {
        if (noVermelho(no.getDireita()))
            no = rotacionarEsquerda(no);
        if (noVermelho(no.getEsquerda()) && noVermelho(no.getDireita()))
            no = rotacionarDireita(no);
        if (noVermelho(no.getEsquerda()) && noVermelho(no.getDireita()))
            alterarCor(no);

        no.setSize(1 + size(no.getEsquerda()) + size(no.getDireita()));
        return no;
    }
 
    public void emOrdem() {
        emOrdem(this.raiz);
    }

    private void emOrdem(No atual) {
        if (atual != null) {
            emOrdem(atual.getEsquerda());
            System.out.print(atual.getKey() + "    ");
            emOrdem(atual.getDireita());
        }
    }
}
