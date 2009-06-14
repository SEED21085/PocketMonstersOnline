package org.pokenet.client.backend.entity;

import java.util.ArrayList;

import org.pokenet.client.backend.entity.Enums.Poketype;

/**
 * Represents our player
 * @author shadowkanji
 *
 */
public class OurPlayer extends Player {
	private OurPokemon [] m_pokemon;
	private ArrayList<PlayerItem> m_items;
    private String[] m_badges = new String[0];
	private int m_money;
	
	/**
	 * Default constructor
	 */
	public OurPlayer() {
		m_pokemon = new OurPokemon[6];
		m_items = new ArrayList<PlayerItem>();
		m_badges = new String[0];
		m_money = 0;
	}
	
	/**
	 * Constructor to be used if our player already exists
	 * @param original
	 */
	public OurPlayer(OurPlayer original) {
		m_pokemon = original.getPokemon();
		m_items = original.getItems();
		m_sprite = original.getSprite();
		m_username = original.getUsername();
		m_isAnimating = original.isAnimating();
	}
	
	public void set(Player p) {
		m_x = p.getX();
		m_y = p.getY();
		m_svrX = p.getServerX();
		m_svrY = p.getServerY();
		m_sprite = p.getSprite();
		m_direction = p.getDirection();
		m_username = p.getUsername();
		m_id = p.getId();
		m_ours = p.isOurPlayer();
	}
	
	/**
	 * Returns our player's party
	 * @return
	 */
	public OurPokemon[] getPokemon() {
		return m_pokemon;
	}
	
	/**
	 * Returns our player's bag
	 * @return
	 */
	public ArrayList<PlayerItem> getItems() {
		return m_items;
	}
	
	/**
	 * Adds an item to this player's bag (automatically handles if its in the bag already)
	 * @param number
	 * @param quantity
	 */
	public void addItem(int number, int quantity) {
		boolean exists = false;
		for(int i = 0; i < m_items.size(); i++) {
			if(m_items.get(i) != null && m_items.get(i).getNumber() == number) {
				m_items.get(i).setQuantity(quantity);
				exists = true;
			}
		}
		if(!exists){
			m_items.add(new PlayerItem(number, quantity));
		}
	}
	
	/**
	 * Removes an item from this player's bag
	 * @param number
	 * @param quantity
	 */
	public void removeItem(int number, int quantity) {
		for(int i = 0; i < m_items.size(); i++) {
			if(m_items.get(i) != null && m_items.get(i).getNumber() == number) {
				if(m_items.get(i).getQuantity() - quantity > 0)
					m_items.get(i).setQuantity(m_items.get(i).getQuantity() - quantity);
				else
					m_items.remove(i);
				return;
			}
		}
	}
	
	/**
	 * Gets item quantity from bag. 
	 * @param number
	 */
	public int getItemQuantity(int number) {
		int quantity = 0;
		for(int i = 0; i < m_items.size(); i++) {
			if(m_items.get(i) != null && m_items.get(i).getItem().getId() == number) {
				quantity = m_items.get(i).getQuantity(); //Return quantity
				return quantity;
			} else {
				quantity = 0; //Player doesnt own item
			}
		}
		return quantity;
	}
	
	/**
	 * Sets a pokemon in this player's party
	 * @param i
	 * @param information
	 */
	public void setPokemon(int i, String [] info) {
		/*
		 * Set sprite, name, gender and hp
		 */
		System.out.println(info.length);
		m_pokemon[i] = new OurPokemon();
		m_pokemon[i].setSpriteNumber(Integer.parseInt(info[0]));
		m_pokemon[i].setName(info[1]);
		m_pokemon[i].setCurHP(Integer.parseInt(info[5]));
		m_pokemon[i].setGender(Integer.parseInt(info[3]));
		if(info[4].equalsIgnoreCase("0"))
			m_pokemon[i].setShiny(false);
		else
			m_pokemon[i].setShiny(true);
		m_pokemon[i].setMaxHP(Integer.parseInt(info[2]));
		/*
		 * Stats
		 */
		m_pokemon[i].setAtk(Integer.parseInt(info[6]));
		m_pokemon[i].setDef(Integer.parseInt(info[7]));
		m_pokemon[i].setSpeed(Integer.parseInt(info[8]));
		m_pokemon[i].setSpatk(Integer.parseInt(info[9]));
		m_pokemon[i].setSpdef(Integer.parseInt(info[10]));
		m_pokemon[i].setType1(Poketype.valueOf(info[11]));
		if(info[12] != null && !info[12].equalsIgnoreCase("")) {
			m_pokemon[i].setType2(Poketype.valueOf(info[12]));
		}
		m_pokemon[i].setExp(Integer.parseInt(info[13].substring(0, info[13].indexOf('.'))));
		m_pokemon[i].setLevel(Integer.parseInt(info[14]));
		m_pokemon[i].setAbility(info[15]);
		m_pokemon[i].setNature(info[16]);
		/*
		 * Moves
		 */
		String [] moves = new String[4];
		for(int j = 0; j < 4; j++) {
			if(j < info.length - 17 && info[j + 17] != null)
				moves[j] = info[j + 17];
			else
				moves[j] = "";
		}
		m_pokemon[i].setMoves(moves);
		
		m_pokemon[i].setSprite();
		m_pokemon[i].setBackSprite();
		m_pokemon[i].setIcon();
	}
	
	/**
	 * Returns the player's money
	 * @return
	 */
	public int getMoney(){
		return m_money;
	}
	
	/**
	 * Sets the players money
	 * @param m
	 */
	public void setMoney(int m) {
		m_money = m;
	}
	
	/**
	 * Returns the player's badges
	 */
	public String [] getBadges(){
		return m_badges;
	}
	
	/**
	 * Swaps two pokemon
	 * @param Poke1
	 * @param Poke2
	 */
	public void swapPokemon(int Poke1, int Poke2){
		OurPokemon temp1 = m_pokemon[Poke1];
		m_pokemon[Poke1] = m_pokemon[Poke2];
		m_pokemon[Poke2] = temp1;
	}
	
	public boolean canMove() {
		if(this.isAnimating()) {
			switch(m_direction) {
			case Up:
				return this.getX() == this.getServerX();
			case Down:
				return this.getX() == this.getServerX();
			case Left:
				return this.getY() == this.getServerY();
			case Right:
				return this.getY() == this.getServerY();
			}
		} else {
			if(this.getX() == this.getServerX() && 
					this.getY() == this.getServerY()) {
				return true;
			}
		}
		return false;
	}
}