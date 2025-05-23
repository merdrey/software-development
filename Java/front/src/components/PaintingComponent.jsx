import React, { useState, useEffect } from 'react';
import { connect } from "react-redux";
import BackendService from "../services/BackendService";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faArrowLeft, faSave } from '@fortawesome/free-solid-svg-icons'
import {useNavigate, useParams} from 'react-router'
import { Button, Form } from 'react-bootstrap';
import { alertActions } from '../utils/Rdx';

const PaintingComponent = props => {

    const nav = useNavigate();
    const [hidden, setHidden] = useState(true);
    const [name, setName] = useState('');
    const [artistId, setArtistId] = useState('');
    const [museumId, setMuseumId] = useState('');
    const [artists, setArtists] = useState([]);
    const [museums, setMuseums] = useState([]);
    const id = useParams().id;

    const handleSubmit = e => {
        e.preventDefault();
        
        let err = null;

        if (name == "") {
            err = 'Пустое поле ввода'
        }

        if (artistId == "") {
            err = "Выберите художника"
        }

        if (museumId == "") {
            err = "Выберите музей"
        }

        if (err != null) {
            props.dispatch(alertActions.error(err));
        } 
        else {
            if (id == -1) {
            let painting = {
                name: name,
                artist: {
                    id: artistId
                },
                museum: {
                    id: museumId
                }
            }
            BackendService.createPainting(painting)
            .then(nav('/paintings'))
            .catch(() => {
            })

        } 
        else {
            let painting = {
                id: id,
                name: name,
                artist: {
                    id: artistId
                },
                museum: {
                    id: museumId
                }
            }
            BackendService.updatePainting(painting)
            .then(nav('/paintings'))
            .catch(() => {
            })
        }
        }
    }

    const onBackButtonClicked = () => {
        nav('/paintings');
    }

    const getPainting = () => {        
        if (id != -1) {
            BackendService.retrievePainting(id).then(resp => {
                setName(resp.data.name);
                setArtistId(resp.data.artist.id);
                setMuseumId(resp.data.museum.id);
                setHidden(false);
            })
            .catch(() => {
                setHidden(true)
            })
        } 
        else {
            setHidden(false)
        }
    }

    useEffect(() => {
            BackendService.retrieveAllArtists(0, 1000)
            .then(resp => {
                setArtists(resp.data.content)
            });
            BackendService.retrieveAllMuseums(0, 1000)
            .then(resp => {
                setMuseums(resp.data.content)
            })
            getPainting();
        }, [])
    
    if (hidden)
        return null;
    return (
         <div className="m-4">
            <div className="row my-2">
                <div className='col'>
                    <h3>Картина</h3>
                </div>
                <div className="btn-toolbar col">
                    <div className="btn-group ms-auto">
                        <button className="btn btn-outline-secondary"
                                onClick={onBackButtonClicked}>
                            <FontAwesomeIcon icon={faArrowLeft} />{' '}Назад
                        </button>
                    </div>
                </div>
                
            </div>
            <Form onSubmit={handleSubmit}>
                <Form.Group className='mb-3'>
                    <Form.Label>
                        Название
                    </Form.Label>
                    <Form.Control 
                        type='text'
                        value={name} 
                        name="name" 
                        onChange={(e) => setName(e.target.value)} 
                        autoComplete='off'>
                    </Form.Control>
                    <Form.Label>
                        Художник
                    </Form.Label>
                    <Form.Select 
                        value={artistId} 
                        onChange = {(e) => setArtistId(e.target.value)}>
                            <option value="">Выберите художника</option>
                            {artists && artists.length > 0 
                            ? (
                                artists.map(artist => (
                            <option key={artist.id} value={artist.id}>
                                {artist.name}
                            </option>))) 
                            : (<option value="" disabled>Загрузка художников...</option>)}
                    </Form.Select>
                    <Form.Label>
                        Музей
                    </Form.Label>
                    <Form.Select 
                        value={museumId} 
                        onChange = {(e) => setMuseumId(e.target.value)}>
                            <option value="">Выберите музей</option>
                            {museums && museums.length > 0 
                            ? (
                                museums.map(museum => (
                            <option key={museum.id} value={museum.id}>
                                {museum.name}
                            </option>))) 
                            : (<option value="" disabled>Загрузка музеев...</option>)}
                    </Form.Select>
                </Form.Group>
                <Button type='submit' variant='outline-secondary'>
                    <FontAwesomeIcon icon={faSave}/>{' '}Сохранить
                </Button>
            </Form>
        </div>
    );
}

export default connect()(PaintingComponent);